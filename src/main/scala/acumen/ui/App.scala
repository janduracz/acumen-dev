package acumen
package ui

import tl._
import interpreter._
import java.lang.Thread
import scala.actors._
import collection.JavaConversions._
import java.awt.Font
import java.awt.Color
import java.awt.RenderingHints
import java.awt.GraphicsEnvironment
import java.awt.Desktop
import java.io._
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.undo._
import javax.swing.text._
import javax.swing.KeyStroke
import javax.swing.event.DocumentListener
import javax.swing.event.DocumentEvent
import swing._
import swing.event._
import java.awt.KeyboardFocusManager
import java.awt.KeyEventDispatcher
import java.awt.event.KeyEvent
import scala.Boolean
import java.awt.Toolkit

// class Acumen = Everything that use to be GraphicalMain.  Graphical
// Main can't be an object it will cause Swing components to be
// initialized outside of the Event Dispatcher Thread (EDT).  Thus 
// we make it a seperate class and have GraphicalMain top() method
// initialize it

class App extends SimpleSwingApplication {
  import App._

  App.ui = this

  // Create a special actor to listen to events from other threads

  case object EXIT
  val actor = new Actor with Publisher {
    override val scheduler = new SchedulerAdapter {
      def execute(fun: => Unit) { Swing.onEDT(fun) }
    }
    
    def act() {
      Supervisor.watch(this, "Main UI", {restart})
      loop {
        react {
          case Error(e)       => reportError(e)
          case Progress(p)    => statusZone.setProgress(p)
          case ProgressMsg(m) => console.log(m); console.newLine
          case Progress3d(p)  => threeDtab.setProgress(p)

          case EXIT => exit

          case SendInit => controller ! Init(codeArea.syntaxTextArea.getText, interpreter)

          case msg : Event => 
            //println("Publishing This Msg: " + msg)
            publish(msg)

          case msg => println("Unknown Msg in GM: " + msg)
        }
      }
    }
  }
  
  App.actor = actor
  App.pub = actor

  /* ---- state variables ---- */
  val controller = new Controller
  var lastNumberOfThreads = 2
  
//**************************************
//**************************************
   

  /* ----- UI setup ------- */

  /* 1. left pane */
  /* 1.1 upper pane */
  val upperButtons = new ControlButtons

  val codeArea = new CodeArea

  val statusZone = new StatusZone
  val upperBottomPane = new BoxPanel(Orientation.Horizontal) {
    contents += upperButtons
    contents += statusZone
  }
  val upperPane = new BorderPanel {
    add(codeArea.filenameLabel, BorderPanel.Position.North)
    add(new ScrollPane(codeArea), BorderPanel.Position.Center)
    add(upperBottomPane,  BorderPanel.Position.South) 
  }
  
  /* 1.2 lower pane */
  val console = new tl.Console

  val lowerPane = new BorderPanel {
    add(new Label("Console"), BorderPanel.Position.North)
    add(new ScrollPane(console), BorderPanel.Position.Center)
  }

  val leftPane = 
    new SplitPane(Orientation.Horizontal, upperPane, lowerPane) { 
      oneTouchExpandable = true
      resizeWeight = 0.9
    }

  /* 2 right pane */
  val traceTable = new Table { 
    //model = traceModel
    autoResizeMode = Table.AutoResizeMode.Off
  }

  // FIXME: This probably should't be here -- kevina
  val jPlotI = new plot.JPlotInput {
    import interpreters.enclosure._
    val rnd = new Rounding(10)
    def newData() = controller.model.getNewData
    def addToPlot(d: Object) = {
      newPlotView.plotter.plot(d.asInstanceOf[Seq[UnivariateAffineEnclosure]],null)(rnd)
      newPlotView.plotPanel.validate
    }
  }

  val plotView = new plot.PlotTab
  val pointedView = new plot.PointedView(plotView)

  val plotTab = new BorderPanel {
    add(new FlowPanel(FlowPanel.Alignment.Leading)(pointedView), 
	BorderPanel.Position.North)
    add(plotView, BorderPanel.Position.Center)
  }
  lazy val newPlotView = new plot.JFreePlotTab
  val newPlotTab = if (GraphicalMain.disableNewPlot) {
    null 
  } else {
    new BorderPanel {
      //TODO Implement and add something like pointedView for the new plotting code
      add(newPlotView, BorderPanel.Position.Center)
    }
  }
  jPlotI.enabled = newPlotTab != null

  val traceTab = new ScrollPane(traceTable) 
  var threeDtab = if (GraphicalMain.disable3D) {
    console.log("Acumen3D disabled.")
    console.newLine
    new threeD.DisabledThreeDTab
  } else try {
    new threeD.ThreeDTab(controller)
  } catch {
    case e:UnsatisfiedLinkError => 
      console.log("Error loading Java3D: " + e)
      console.newLine
      console.log("Disabling 3D Tab.")
      console.newLine
      new threeD.DisabledThreeDTab
  }
  
  val views = new TabbedPane {
    assert(pages.size == 0)
    pages += new TabbedPane.Page("Plot",     plotTab)
    val PLOT_IDX = pages.last.index
    val NEW_PLOT_IDX = if (newPlotTab != null) {
      pages += new TabbedPane.Page("New Plot", newPlotTab)
      selection.index = pages.last.index
      pages.last.index
    } else {
      -1
    }
    pages += new TabbedPane.Page("Trace",    traceTab)
    val TABLE_IDX = pages.last.index
    
    pages += new TabbedPane.Page("3D",       threeDtab)
    if (!threeDtab.enableTab)
      pages.last.enabled = false

    selection.reactions += {
      case SelectionChanged(_) =>
        actor.publish(ViewChanged(selection.index))
    }
  }

  /* main component */
  val body = 
    new SplitPane(Orientation.Vertical, leftPane, views) { 
      oneTouchExpandable = true
      resizeWeight = 0.2
    }
  
  /* menu bar */

  val enabledWhenStopped = scala.collection.mutable.Buffer[MenuItem]()
 
  val bar = new MenuBar {
    contents += new Menu("File") {
      mnemonic = Key.F
      contents += new MenuItem(Action("New")({ codeArea.newFile })) 
                      { mnemonic = Key.N; enabledWhenStopped += this}
      contents += new MenuItem(Action("Open")({ codeArea.openFile(codeArea.currentDir) })) 
                      { mnemonic = Key.O; enabledWhenStopped += this}
      contents += new MenuItem(Action("Save")(codeArea.saveFile))
                      { mnemonic = Key.S }
      contents += new MenuItem(Action("Save As")(codeArea.saveFileAs))
                      { mnemonic = Key.A }
      contents += new MenuItem(Action("Recover")({ codeArea.openFile(Files.autoSavedDir) }))
                      { mnemonic = Key.R; enabledWhenStopped += this}
      contents += new MenuItem(Action("Exit")(exit))
                      { mnemonic = Key.E }
    }

    contents += new Menu("Plotting") {
      
      mnemonic = Key.P
      contents += new Menu("Style") {
        val rb1 = new RadioMenuItem("") {
          selected = true
          action = Action("Lines") { plotView.setPlotStyle(plot.Lines()) }
        }
        val rb2 = new RadioMenuItem("") {
          selected = false
          action = Action("Dots") { plotView.setPlotStyle(plot.Dots()) }
        }
        val rb3 = new RadioMenuItem("") {
          selected = false
          action = Action("Both") { plotView.setPlotStyle(plot.Both()) }
        }
        contents ++= Seq(rb1,rb2,rb3)
        new ButtonGroup(rb1,rb2,rb3)
      }
      contents += new CheckMenuItem("") {
        selected = false
        action = Action("Automatically plot simulator fields") { 
                    plotView.toggleSimulator(this.selected) 
                }
      }
      contents += new CheckMenuItem("") {
        selected = false
        action = Action("Automatically plot child counter fields") { 
                    plotView.toggleNextChild(this.selected) 
                }
      }
      contents += new CheckMenuItem("") {
        selected = false
        action = Action("Automatically random number generator seeds") { 
                    plotView.toggleSeeds(this.selected)
                }
      }
    }

    contents += new Menu("Semantics") {
      mnemonic = Key.S
      val rb1 = new RadioMenuItem("") {
        selected = !GraphicalMain.useEnclosures
        action = Action("Purely Functional") { setInterpreter(new CStoreCntrl(interpreters.reference.Interpreter)) }
        enabledWhenStopped += this
      }
      val rb2 = new RadioMenuItem("") {
        selected = false
        action = Action("Imperative (Parallel)") {
          def diag = Dialog.showInput(
            body, "Choose a number of threads",
            "Parallel Interpreter", Dialog.Message.Question,
            Swing.EmptyIcon, Seq(), lastNumberOfThreads.toString)
          def go: Unit = try {
            def n: String = diag.getOrElse(n)
            lastNumberOfThreads = Integer.parseInt(n)
            setInterpreter(new CStoreCntrl(new interpreters.parallel.Interpreter(lastNumberOfThreads)))
            console.log("Number of threads set to " + lastNumberOfThreads + ".")
          } catch {
            case _ =>
              console.logError("Bad number of threads.")
              go
          }
          go
        }
        enabledWhenStopped += this
      }
      val rb3 = new RadioMenuItem("") {
        selected = GraphicalMain.useEnclosures
        action = Action("Enclosure") { setInterpreter(new EnclosureCntrl(interpreters.enclosure.Interpreter)) }
        enabledWhenStopped += this
      }
      contents ++= Seq(rb1,rb2,rb3)
      new ButtonGroup(rb1,rb2,rb3)
    }
   
    contents += new Menu("Help") {
      mnemonic = Key.H
      contents += new MenuItem(Action("Tutorial")(tutorial))
                      { mnemonic = Key.T }
      contents += new MenuItem(Action("About")(about))
                      { mnemonic = Key.A }
    }
  }
 
  /* gluing everything together */
  def top = new MainFrame {
    title = "Acumen"
    contents = body
    menuBar = bar
    size = new Dimension(1024,768)
    // XXX: consider deleting
    override def closeOperation() {     
    exit
     }
    
  }

  def exit {  
    //timer3d.destroy=true
    //receiver.destroy=true
    //threeDView.exit
    
    if (!codeArea.editedSinceLastSave || codeArea.confirmContinue(body.peer)) {
      controller ! Stop
      actor ! EXIT				
      quit
    }
  }

  def withErrorReporting(action: => Unit) : Unit = {
    try action 
    catch { case e => reportError(e) }
  }

  def reportError(e:Throwable) {
    val em = e.getMessage
    console.logError(if (em!=null) em else e.toString)
    System.err.println("Note: Redirected this exception to console log:")
    e.printStackTrace()
  }

  //def redraw = traceView.redraw

  /* ------ simple dialogs ----- */

  def about = { 
    val version = acumen.util.System.version
    Dialog.showMessage(body, "Acumen " + version, "About")
  }
  def tutorial = {
    if (Desktop.isDesktopSupported) {
      val desktop = Desktop.getDesktop
      try { desktop.browse(acumen.util.System.tutorialUrl) } 
      catch { case e:IOException => reportError(e) }
    } else reportError(new Exception("Could not find a web browser."))
  }

  /* ----- events handling ---- */
  
  var state : State = Stopped
  var interpreter : InterpreterCntrl = 
    if (GraphicalMain.useEnclosures)
      new EnclosureCntrl(interpreters.enclosure.Interpreter)
    else
      new CStoreCntrl(interpreters.reference.Interpreter)
  def setInterpreter(i : InterpreterCntrl) = {
    interpreter = i
  }

  controller.start()

  listenTo(actor)
  // disable and enable menu items
  reactions += {
    case st:State =>
      st match {
        case Stopped =>
          for (el <- enabledWhenStopped) el.enabled = true
        case _ =>
          for (el <- enabledWhenStopped) el.enabled = false
      }
  }
  // update console
  reactions += {
    case st:State => 
      //println("GM: New State: " + st)
      st match {
        case Stopped =>
          console.log("Stopped.")
          console.newLine
        case Paused =>
          console.log("Paused. ")
          console.newLine
        case Starting =>
          console.log("Starting...")
        case Resuming if state != Starting =>
          console.log("Resuming...")
        case _ =>
      }
      state = st
  }
          
  
  // FIXME: Move me into a seperate TraceTable class
  // and move tableI out of plotter and into the new class
  val defaultTableModel = traceTable.model
  traceTable.listenTo(actor)
  traceTable.reactions += {
    case st:State => 
      st match {
        case Starting => 
          traceTable.model = defaultTableModel
        //case _:Ready if traceTable.model.isInstanceOf[TraceModel] => 
        //  traceTable.model.asInstanceOf[TraceModel].fireTableStructureChanged()
        case _ => 
      }
    case plot.TraceModelReady(model) =>
      traceTable.model = model
      model.fireTableStructureChanged()
    case ViewChanged(idx) =>
      if (idx == views.TABLE_IDX) {
        plotView.plotPanel.tableI.enabled = true
        plotView.plotPanel.plotter ! plot.Refresh
      } else {
        plotView.plotPanel.tableI.enabled = false
      }
  }
  
  // Add application-wide keyboard shortcuts
  
  KeyboardFocusManager.getCurrentKeyboardFocusManager.addKeyEventDispatcher(new KeyEventDispatcher {
      def dispatchKeyEvent(e: KeyEvent): Boolean =
        if (e.getModifiers == Toolkit.getDefaultToolkit.getMenuShortcutKeyMask ||
            e.getModifiers == java.awt.event.InputEvent.CTRL_MASK)
            e.getKeyCode match {
              case java.awt.event.KeyEvent.VK_R  => upperButtons.bPlay.doClick ; true
              case java.awt.event.KeyEvent.VK_T  => upperButtons.bStop.doClick ; true
              case java.awt.event.KeyEvent.VK_G  => upperButtons.bStep.doClick ; true
              case _                             => false 
            }
        else false 
    })

  /* ----- initialisation ----- */

  actor.start
  codeArea.listenDocument
  console.log("<html>Welcome to Acumen.<br/>"+
              "Please see LICENSE file for licensing details.</html>")
  console.newLine
  actor.publish(Stopped)

  if (GraphicalMain.autoPlay)
    upperButtons.bPlay.doClick
}

object App {
  def init = {
    new App // the initialization will set the members below
  }

  var ui : App = null

  var actor : Actor = null
  var pub : Publisher = null

  def ! (e: Any) = actor ! e
  def publish(e: Event) = pub.publish(e)

  sealed abstract class State extends Event
  sealed abstract class Ready extends State
  sealed abstract class Playing extends State
  case object Starting extends Playing
  case object Resuming extends Playing
  case object Stopped extends Ready
  case object Paused extends Ready

  case class ViewChanged(idx: Int) extends Event
}