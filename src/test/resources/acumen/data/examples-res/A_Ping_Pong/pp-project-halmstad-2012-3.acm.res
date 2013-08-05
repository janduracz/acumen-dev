#0 {
  className = Main,
  game = #0.1,
  mode = "Round1",
  name1 = "PingPing",
  name2 = "ParadoX",
  simulator = #0.0,
  t = 0,
  t' = 1
}
#0.0 { className = Simulator, time = 0.000000 }
#0.1 {
  _3D = [["Text", [-2.800000, 6.500000, -4], 1, [0.300000, 0, 0], [1.700000, 0, 0], ""], ["Text", [2.800000, 6.500000, -4], 1, [0.300000, 0.300000, 0.300000], [1.700000, 0, 0], ""], ["Text", [-1.800000, 6.500000, -0.500000], 1, [0.600000, 0, 0], [1.700000, 0, 0], ""], ["Text", [1.800000, 6.500000, -0.500000], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], ""]],
  ball = #0.1.0,
  ballActuator = #0.1.2,
  ballob = #0.1.1,
  bat1 = #0.1.7,
  bat2 = #0.1.8,
  batActuator1 = #0.1.3,
  batActuator2 = #0.1.4,
  className = Game,
  finish = false,
  maxEnergy = 18,
  mode = "Player2Serve",
  name1 = "PingPing",
  name2 = "ParadoX",
  player1 = #0.1.11,
  player1Score = 0,
  player2 = #0.1.12,
  player2Score = 0,
  referee = #0.1.10,
  serveNumber = 2,
  t = 0,
  t' = 1,
  table = #0.1.9
}
#0.1.0 {
  _3D = ["Sphere", [0, 0, 0.500000], 0.030000, [1, 1, 1], [0, 0, 0]],
  className = Ball,
  k2 = 0.166667,
  k_z = [1, 1, -0.990000],
  mode = "Fly",
  p = [0, 0, 0.500000],
  p' = [5, 1, -3],
  p'' = [0, 0, 0]
}
#0.1.1 {
  ap = [0, 0, 0],
  className = BallObserver,
  mode = "Sample",
  p = [0, 0, 0],
  pp = [0, 0, 0],
  t = 0,
  t' = 1,
  v = [0, 0, 0]
}
#0.1.2 {
  action = 0,
  angle = [0, 0, 0],
  className = BallActuator,
  done = false,
  mode = "Initialize",
  v1 = [0, 0, 0],
  v2 = [0, 0, 0],
  v3 = [0, 0, 0]
}
#0.1.3 {
  angle = [0, 0, 0],
  className = BatActuator,
  energy = 0,
  energy' = 0,
  p = [-1.600000, 0, 0.200000],
  p' = [0, 0, 0],
  p'' = [0, 0, 0],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.4 {
  angle = [0, 0, 0],
  className = BatActuator,
  energy = 0,
  energy' = 0,
  p = [1.600000, 0, 0.200000],
  p' = [0, 0, 0],
  p'' = [0, 0, 0],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.5 {
  a = [0, 0, 0],
  aTemp = [0, 0, 0],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0, 0, 0.100000],
  batAngle' = [0, 0, 0],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [-1.600000, 0, 0.200000],
  desiredBatP' = [0, 0, 0],
  estimatedBatV = [0, 0, 0],
  estimatedBatV' = [0, 0, 0],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "WiffWaff",
  number = 1,
  reStart = true,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0,
  t' = 1,
  v = [0, 0, 0],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.6 {
  a = [0, 0, 0],
  aTemp = [0, 0, 0],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0, 0, 0.100000],
  batAngle' = [0, 0, 0],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [1.600000, 0, 0.200000],
  desiredBatP' = [0, 0, 0],
  estimatedBatV = [0, 0, 0],
  estimatedBatV' = [0, 0, 0],
  hit = false,
  mode = "Wait",
  n = 2,
  n1 = 0,
  name = "WiffWaff",
  number = 2,
  reStart = true,
  serve = false,
  startPoint = [1.600000, 0, 0.200000],
  t = 0,
  t' = 1,
  v = [0, 0, 0],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.7 {
  _3D = ["Cylinder", [-1.600000, 0, 0.200000], [0.150000, 0.010000], [0.100000, 0.100000, 0.100000], [0, 0, 0.500000]],
  angle = [0, 0, 0.100000],
  className = Bat,
  displayAngle = [0, 0, 0],
  mode = "Run",
  n = 1,
  p = [-1.600000, 0, 0.200000],
  p' = [0, 0, 0],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.8 {
  _3D = ["Cylinder", [1.600000, 0, 0.200000], [0.150000, 0.010000], [0.100000, 0.100000, 0.100000], [0, 0, 0.500000]],
  angle = [0, 0, 0.100000],
  className = Bat,
  displayAngle = [0, 0, 0],
  mode = "Run",
  n = 2,
  p = [1.600000, 0, 0.200000],
  p' = [0, 0, 0],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.9 {
  _3D = [["Box", [0, 0, -0.050000], [3, 1.500000, 0.030000], [0.100000, 0.100000, 1.000000], [0, 0, 0]], ["Box", [-1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [-1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [0, 0, 0.105000], [0.050000, 1.500000, 0.250000], [0.200000, 0.800000, 0.200000], [0, 0, 0]], ["Box", [0, 0, 0], [3, 0.020000, 0.000000], [1, 1, 1], [0, 0, 0]]],
  className = Table
}
#0.1.10 {
  acknowledged = 0,
  bounceTime = 0,
  bounced = false,
  checked = false,
  className = Referee,
  lastHit = 0,
  mode = "Initialize",
  player1Score = 0,
  player2Score = 0,
  reason = "Nothing",
  restart = 0,
  serveNumber = 2,
  status = "Normal",
  t = 0,
  t' = 1,
  x = 0,
  x' = 0,
  y = 0,
  z = 0,
  z' = 0
}
#0.1.11 {
  a = [0, 0, 0],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0, 0, 0.100000],
  batAngle' = [0, 0, 0],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = PingPing,
  count = 0,
  desiredBatP = [-1.600000, 0, 0.200000],
  desiredBatP' = [0, 0, 0],
  estimatedBatV = [0, 0, 0],
  estimatedBatV' = [0, 0, 0],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "PingPing",
  number = 1,
  scount = 0,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0,
  t' = 1,
  v = [0, 0, 0],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.12 {
  D = [1.600000, 0, 0],
  Dflag = false,
  Dt = 0,
  a = [0, 0, 0],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0, 0, 0.100000],
  batAngle' = [0, 0, 0],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = ParadoX,
  count = 0,
  desiredBatP = [1.600000, 0, 0.200000],
  desiredBatP' = [0, 0, 0],
  estimatedBatV = [0, 0, 0],
  estimatedBatV' = [0, 0, 0],
  hit = false,
  hitBack = false,
  mode = "Wait",
  n = 2,
  n1 = 0,
  name = "ParadoX",
  number = 2,
  serve = false,
  startPoint = [1.600000, 0, 0.200000],
  t = 0,
  t' = 1,
  v = [0, 0, 0],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
------------------------------0
#0 {
  className = Main,
  game = #0.1,
  mode = "Round1",
  name1 = "PingPing",
  name2 = "ParadoX",
  simulator = #0.0,
  t = 0.010000,
  t' = 1
}
#0.0 { className = Simulator, time = 0.010000 }
#0.1 {
  _3D = [["Text", [-2.800000, 6.500000, -4], 1, [0.900000, 0, 0], [1.700000, 0, 0], "PingPing"], ["Text", [2.800000, 6.500000, -4], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], "ParadoX"], ["Text", [-1.500000, 6.500000, -5], 1, [0.600000, 0, 0], [1.700000, 0, 0], 0], ["Text", [4.500000, 6.500000, -5], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], 0]],
  ball = #0.1.0,
  ballActuator = #0.1.2,
  ballob = #0.1.1,
  bat1 = #0.1.7,
  bat2 = #0.1.8,
  batActuator1 = #0.1.3,
  batActuator2 = #0.1.4,
  className = Game,
  finish = false,
  maxEnergy = 18,
  mode = "Player2Serve",
  name1 = "PingPing",
  name2 = "ParadoX",
  player1 = #0.1.11,
  player1Score = 0,
  player2 = #0.1.12,
  player2Score = 0,
  referee = #0.1.10,
  serveNumber = 2,
  t = 0.010000,
  t' = 1,
  table = #0.1.9
}
#0.1.0 {
  _3D = ["Sphere", [0, 0, 0.500000], 0.030000, [1, 1, 1], [0, 0, 0]],
  className = Ball,
  k2 = 0.166667,
  k_z = [1, 1, -0.990000],
  mode = "Fly",
  p = [0.049507, 0.009901, 0.469316],
  p' = [4.950699, 0.990140, -3.068420],
  p'' = [-4.930066, -0.986013, -6.841960]
}
#0.1.1 {
  ap = [0, 0, 0],
  className = BallObserver,
  mode = "Estimate0",
  p = [0, 0, 0.500000],
  pp = [0, 0, 0.500000],
  t = 0,
  t' = 1,
  v = [0, 0, 0]
}
#0.1.2 {
  action = 0,
  angle = [0, 0, 0],
  className = BallActuator,
  done = false,
  mode = "Initialize",
  v1 = [0, 0, 0],
  v2 = [0, 0, 0],
  v3 = [0, 0, 0]
}
#0.1.3 {
  angle = [0, 0, 0.100000],
  className = BatActuator,
  energy = 0.000000,
  energy' = 0.000000,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0.000000, 0.000000, 0.000000],
  p'' = [0, 0, 0],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.4 {
  angle = [0, 0, 0.100000],
  className = BatActuator,
  energy = 0.000000,
  energy' = 0.000000,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0.000000, 0.000000, 0.000000],
  p'' = [0, 0, 0],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.5 {
  a = [-203.200000, 0.000000, 0.000000],
  aTemp = [-203.200000, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.099000],
  batAngle' = [0.000000, 0.000000, -0.100000],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [-1.632000, 0.000000, 0.200000],
  desiredBatP' = [-3.200000, 0.000000, 0.000000],
  estimatedBatV = [-2.032000, 0.000000, 0.000000],
  estimatedBatV' = [-203.200000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "WiffWaff",
  number = 1,
  reStart = true,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0.010000,
  t' = 1,
  v = [-3.200000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.6 {
  a = [0.000000, 0.000000, 0.000000],
  aTemp = [0.000000, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.099000],
  batAngle' = [0.000000, 0.000000, -0.100000],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 2,
  n1 = 0,
  name = "WiffWaff",
  number = 2,
  reStart = true,
  serve = false,
  startPoint = [1.600000, 0, 0.200000],
  t = 0.010000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.7 {
  _3D = ["Cylinder", [-1.650000, 0.000000, 0.200000], [0.150000, 0.001000], [1, 0.100000, 0.100000], [-0.157000, -0.000000, -1.570000]],
  angle = [0, 0, 0.100000],
  className = Bat,
  displayAngle = [0.157000, 0.000000, 1.570000],
  mode = "Run",
  n = 1,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.8 {
  _3D = ["Cylinder", [1.650000, 0.000000, 0.200000], [0.150000, 0.001000], [0.100000, 0.100000, 0.100000], [0.000000, 1.570000, 1.570000]],
  angle = [0, 0, 0.100000],
  className = Bat,
  displayAngle = [0.000000, 1.570000, 1.570000],
  mode = "Run",
  n = 2,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.9 {
  _3D = [["Box", [0, 0, -0.050000], [3, 1.500000, 0.030000], [0.100000, 0.100000, 1.000000], [0, 0, 0]], ["Box", [-1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [-1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [0, 0, 0.105000], [0.050000, 1.500000, 0.250000], [0.200000, 0.800000, 0.200000], [0, 0, 0]], ["Box", [0, 0, 0], [3, 0.020000, 0.000000], [1, 1, 1], [0, 0, 0]]],
  className = Table
}
#0.1.10 {
  acknowledged = 0,
  bounceTime = 0,
  bounced = false,
  checked = false,
  className = Referee,
  lastHit = 0,
  mode = "Initialize",
  player1Score = 0,
  player2Score = 0,
  reason = "Nothing",
  restart = 0,
  serveNumber = 2,
  status = "Normal",
  t = 0.010000,
  t' = 1,
  x = 0.050000,
  x' = 5.000000,
  y = 0.000000,
  z = 0.470000,
  z' = -3.000000
}
#0.1.11 {
  a = [0.000000, 0.000000, 0.000000],
  ballp = [0, 0, 0.500000],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.099000],
  batAngle' = [0.000000, 0.000000, -0.100000],
  batp = [-1.600000, 0, 0.200000],
  bounced = false,
  className = PingPing,
  count = 0,
  desiredBatP = [-1.600000, 0.000000, 0.200000],
  desiredBatP' = [0, 0, 0],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "PingPing",
  number = 1,
  scount = 0,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0.010000,
  t' = 1,
  v = [0, 0, 0],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.12 {
  D = [1.600000, 0, 0],
  Dflag = true,
  Dt = 0,
  a = [0.000000, 0.000000, 0.000000],
  ballp = [0, 0, 0.500000],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.099000],
  batAngle' = [0.000000, 0.000000, -0.100000],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = ParadoX,
  count = 0,
  desiredBatP = [1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  hitBack = false,
  mode = "Prepare",
  n = 2,
  n1 = 0,
  name = "ParadoX",
  number = 2,
  serve = true,
  startPoint = [1.600000, 0, 0.200000],
  t = 0.010000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
------------------------------2
#0 {
  className = Main,
  game = #0.1,
  mode = "Round1",
  name1 = "PingPing",
  name2 = "ParadoX",
  simulator = #0.0,
  t = 0.020000,
  t' = 1
}
#0.0 { className = Simulator, time = 0.020000 }
#0.1 {
  _3D = [["Text", [-2.800000, 6.500000, -4], 1, [0.900000, 0, 0], [1.700000, 0, 0], "PingPing"], ["Text", [2.800000, 6.500000, -4], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], "ParadoX"], ["Text", [-1.500000, 6.500000, -5], 1, [0.600000, 0, 0], [1.700000, 0, 0], 0], ["Text", [4.500000, 6.500000, -5], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], 0]],
  ball = #0.1.0,
  ballActuator = #0.1.2,
  ballob = #0.1.1,
  bat1 = #0.1.7,
  bat2 = #0.1.8,
  batActuator1 = #0.1.3,
  batActuator2 = #0.1.4,
  className = Game,
  finish = false,
  maxEnergy = 18,
  mode = "Player2Serve",
  name1 = "PingPing",
  name2 = "ParadoX",
  player1 = #0.1.11,
  player1Score = 0,
  player2 = #0.1.12,
  player2Score = 0,
  referee = #0.1.10,
  serveNumber = 2,
  t = 0.020000,
  t' = 1,
  table = #0.1.9
}
#0.1.0 {
  _3D = ["Sphere", [0.049507, 0.009901, 0.469316], 0.030000, [1, 1, 1], [0, 0, 0]],
  className = Ball,
  k2 = 0.166667,
  k_z = [1, 1, -0.990000],
  mode = "Fly",
  p = [0.098527, 0.019705, 0.437954],
  p' = [4.901951, 0.980390, -3.136206],
  p'' = [-4.874826, -0.974965, -6.778606]
}
#0.1.1 {
  ap = [0.049507, 0.009901, 0.469316],
  className = BallObserver,
  mode = "Sample",
  p = [0.049507, 0.009901, 0.469316],
  pp = [0, 0, 0.500000],
  t = 0,
  t' = 1,
  v = [4.950699, 0.990140, -3.068420]
}
#0.1.2 {
  action = 0,
  angle = [0, 0, 0],
  className = BallActuator,
  done = false,
  mode = "Initialize",
  v1 = [0, 0, 0],
  v2 = [0, 0, 0],
  v3 = [0, 0, 0]
}
#0.1.3 {
  angle = [0.000000, 0.000000, 0.099000],
  className = BatActuator,
  energy = 0.000000,
  energy' = 0.000000,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0.000000, 0.000000, 0.000000],
  p'' = [0.000000, 0.000000, 0.000000],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.4 {
  angle = [0.000000, 0.000000, 0.099000],
  className = BatActuator,
  energy = 0.000000,
  energy' = 0.000000,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0.000000, 0.000000, 0.000000],
  p'' = [0.000000, 0.000000, 0.000000],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.5 {
  a = [-178.384000, 0.000000, 0.000000],
  aTemp = [-178.384000, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.098010],
  batAngle' = [0.000000, 0.000000, -0.099000],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [-1.664000, 0.000000, 0.200000],
  desiredBatP' = [-3.200000, 0.000000, 0.000000],
  estimatedBatV = [-3.815840, 0.000000, 0.000000],
  estimatedBatV' = [-178.384000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "WiffWaff",
  number = 1,
  reStart = true,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0.020000,
  t' = 1,
  v = [-3.200000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.6 {
  a = [0.000000, 0.000000, 0.000000],
  aTemp = [0.000000, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.098010],
  batAngle' = [0.000000, 0.000000, -0.099000],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 2,
  n1 = 0,
  name = "WiffWaff",
  number = 2,
  reStart = true,
  serve = false,
  startPoint = [1.600000, 0, 0.200000],
  t = 0.020000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.7 {
  _3D = ["Cylinder", [-1.650000, 0.000000, 0.200000], [0.150000, 0.001000], [1, 0.100000, 0.100000], [-0.155430, -0.000000, -1.570000]],
  angle = [0.000000, 0.000000, 0.099000],
  className = Bat,
  displayAngle = [0.155430, 0.000000, 1.570000],
  mode = "Run",
  n = 1,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.8 {
  _3D = ["Cylinder", [1.650000, 0.000000, 0.200000], [0.150000, 0.001000], [0.100000, 0.100000, 0.100000], [0.000000, 1.570000, 1.570000]],
  angle = [0.000000, 0.000000, 0.099000],
  className = Bat,
  displayAngle = [0.000000, 1.570000, 1.570000],
  mode = "Run",
  n = 2,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.9 {
  _3D = [["Box", [0, 0, -0.050000], [3, 1.500000, 0.030000], [0.100000, 0.100000, 1.000000], [0, 0, 0]], ["Box", [-1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [-1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [0, 0, 0.105000], [0.050000, 1.500000, 0.250000], [0.200000, 0.800000, 0.200000], [0, 0, 0]], ["Box", [0, 0, 0], [3, 0.020000, 0.000000], [1, 1, 1], [0, 0, 0]]],
  className = Table
}
#0.1.10 {
  acknowledged = 0,
  bounceTime = 0,
  bounced = false,
  checked = false,
  className = Referee,
  lastHit = 0,
  mode = "Initialize",
  player1Score = 0,
  player2Score = 0,
  reason = "Nothing",
  restart = 0,
  serveNumber = 2,
  status = "Normal",
  t = 0.020000,
  t' = 1,
  x = 0.099014,
  x' = 4.950699,
  y = 0.009901,
  z = 0.438632,
  z' = -3.068420
}
#0.1.11 {
  a = [0.000000, 0.000000, 0.000000],
  ballp = [0.049507, 0.009901, 0.469316],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.098010],
  batAngle' = [0.000000, 0.000000, -0.099000],
  batp = [-1.600000, 0.000000, 0.200000],
  bounced = false,
  className = PingPing,
  count = 0,
  desiredBatP = [-1.600000, 0.000000, 0.200000],
  desiredBatP' = [0, 0, 0],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "PingPing",
  number = 1,
  scount = 0,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0.020000,
  t' = 1,
  v = [0, 0, 0],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.12 {
  D = [1.600000, 0, 0],
  Dflag = true,
  Dt = 0,
  a = [0.000000, 0.000000, 0.000000],
  ballp = [0.049507, 0.009901, 0.469316],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.099000],
  batAngle' = [0.000000, 0.000000, -0.100000],
  batp = [1.600000, 0.000000, 0.200000],
  bounced = false,
  className = ParadoX,
  count = 0,
  desiredBatP = [1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  hitBack = false,
  mode = "Prepare",
  n = 2,
  n1 = 0,
  name = "ParadoX",
  number = 2,
  serve = true,
  startPoint = [1.600000, 0, 0.200000],
  t = 0.020000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
------------------------------4
#0 {
  className = Main,
  game = #0.1,
  mode = "Round1",
  name1 = "PingPing",
  name2 = "ParadoX",
  simulator = #0.0,
  t = 0.030000,
  t' = 1
}
#0.0 { className = Simulator, time = 0.030000 }
#0.1 {
  _3D = [["Text", [-2.800000, 6.500000, -4], 1, [0.900000, 0, 0], [1.700000, 0, 0], "PingPing"], ["Text", [2.800000, 6.500000, -4], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], "ParadoX"], ["Text", [-1.500000, 6.500000, -5], 1, [0.600000, 0, 0], [1.700000, 0, 0], 0], ["Text", [4.500000, 6.500000, -5], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], 0]],
  ball = #0.1.0,
  ballActuator = #0.1.2,
  ballob = #0.1.1,
  bat1 = #0.1.7,
  bat2 = #0.1.8,
  batActuator1 = #0.1.3,
  batActuator2 = #0.1.4,
  className = Game,
  finish = false,
  maxEnergy = 18,
  mode = "Player2Serve",
  name1 = "PingPing",
  name2 = "ParadoX",
  player1 = #0.1.11,
  player1Score = 0,
  player2 = #0.1.12,
  player2Score = 0,
  referee = #0.1.10,
  serveNumber = 2,
  t = 0.030000,
  t' = 1,
  table = #0.1.9
}
#0.1.0 {
  _3D = ["Sphere", [0.098527, 0.019705, 0.437954], 0.030000, [1, 1, 1], [0, 0, 0]],
  className = Ball,
  k2 = 0.166667,
  k_z = [1, 1, -0.990000],
  mode = "Fly",
  p = [0.147064, 0.029413, 0.405920],
  p' = [4.853737, 0.970747, -3.203359],
  p'' = [-4.821365, -0.964273, -6.715352]
}
#0.1.1 {
  ap = [0.049507, 0.009901, 0.469316],
  className = BallObserver,
  mode = "Estimate0",
  p = [0.098527, 0.019705, 0.437954],
  pp = [0.098527, 0.019705, 0.437954],
  t = 0,
  t' = 1,
  v = [4.950699, 0.990140, -3.068420]
}
#0.1.2 {
  action = 0,
  angle = [0, 0, 0],
  className = BallActuator,
  done = false,
  mode = "Initialize",
  v1 = [0, 0, 0],
  v2 = [0, 0, 0],
  v3 = [0, 0, 0]
}
#0.1.3 {
  angle = [0.000000, 0.000000, 0.098010],
  className = BatActuator,
  energy = 0.000000,
  energy' = 0.000000,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0.000000, 0.000000, 0.000000],
  p'' = [0.000000, 0.000000, 0.000000],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.4 {
  angle = [0.000000, 0.000000, 0.099000],
  className = BatActuator,
  energy = 0.000000,
  energy' = 0.000000,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0.000000, 0.000000, 0.000000],
  p'' = [0.000000, 0.000000, 0.000000],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.5 {
  a = [-156.794080, 0.000000, 0.000000],
  aTemp = [-156.794080, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.097030],
  batAngle' = [0.000000, 0.000000, -0.098010],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [-1.696000, 0.000000, 0.200000],
  desiredBatP' = [-3.200000, 0.000000, 0.000000],
  estimatedBatV = [-5.383781, 0.000000, 0.000000],
  estimatedBatV' = [-156.794080, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "WiffWaff",
  number = 1,
  reStart = true,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0.030000,
  t' = 1,
  v = [-3.200000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.6 {
  a = [0.000000, 0.000000, 0.000000],
  aTemp = [0.000000, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.097030],
  batAngle' = [0.000000, 0.000000, -0.098010],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 2,
  n1 = 0,
  name = "WiffWaff",
  number = 2,
  reStart = true,
  serve = false,
  startPoint = [1.600000, 0, 0.200000],
  t = 0.030000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.7 {
  _3D = ["Cylinder", [-1.650000, 0.000000, 0.200000], [0.150000, 0.001000], [1, 0.100000, 0.100000], [-0.153876, -0.000000, -1.570000]],
  angle = [0.000000, 0.000000, 0.098010],
  className = Bat,
  displayAngle = [0.153876, 0.000000, 1.570000],
  mode = "Run",
  n = 1,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [-1.600000, 0, 0.200000]
}
#0.1.8 {
  _3D = ["Cylinder", [1.650000, 0.000000, 0.200000], [0.150000, 0.001000], [0.100000, 0.100000, 0.100000], [0.000000, 1.570000, 1.570000]],
  angle = [0.000000, 0.000000, 0.099000],
  className = Bat,
  displayAngle = [0.000000, 1.570000, 1.570000],
  mode = "Run",
  n = 2,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [1.600000, 0, 0.200000]
}
#0.1.9 {
  _3D = [["Box", [0, 0, -0.050000], [3, 1.500000, 0.030000], [0.100000, 0.100000, 1.000000], [0, 0, 0]], ["Box", [-1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [-1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [0, 0, 0.105000], [0.050000, 1.500000, 0.250000], [0.200000, 0.800000, 0.200000], [0, 0, 0]], ["Box", [0, 0, 0], [3, 0.020000, 0.000000], [1, 1, 1], [0, 0, 0]]],
  className = Table
}
#0.1.10 {
  acknowledged = 0,
  bounceTime = 0,
  bounced = false,
  checked = false,
  className = Referee,
  lastHit = 0,
  mode = "Initialize",
  player1Score = 0,
  player2Score = 0,
  reason = "Nothing",
  restart = 0,
  serveNumber = 2,
  status = "Normal",
  t = 0.030000,
  t' = 1,
  x = 0.147546,
  x' = 4.901951,
  y = 0.019705,
  z = 0.406592,
  z' = -3.136206
}
#0.1.11 {
  a = [0.000000, 0.000000, 0.000000],
  ballp = [0.098527, 0.019705, 0.437954],
  ballv = [4.950699, 0.990140, -3.068420],
  batAngle = [0.000000, 0.000000, 0.097030],
  batAngle' = [0.000000, 0.000000, -0.098010],
  batp = [-1.600000, 0.000000, 0.200000],
  bounced = false,
  className = PingPing,
  count = 0,
  desiredBatP = [-1.600000, 0.000000, 0.200000],
  desiredBatP' = [0, 0, 0],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "PingPing",
  number = 1,
  scount = 0,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 0.030000,
  t' = 1,
  v = [0, 0, 0],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.1.12 {
  D = [1.600000, 0, 0],
  Dflag = true,
  Dt = 0,
  a = [0.000000, 0.000000, 0.000000],
  ballp = [0.098527, 0.019705, 0.437954],
  ballv = [4.950699, 0.990140, -3.068420],
  batAngle = [0.000000, 0.000000, 0.099000],
  batAngle' = [0.000000, 0.000000, -0.100000],
  batp = [1.600000, 0.000000, 0.200000],
  bounced = false,
  className = ParadoX,
  count = 0,
  desiredBatP = [1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  hitBack = false,
  mode = "Prepare",
  n = 2,
  n1 = 0,
  name = "ParadoX",
  number = 2,
  serve = true,
  startPoint = [1.600000, 0, 0.200000],
  t = 0.030000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
------------------------------6
#0 {
  className = Main,
  game = #0.2,
  mode = "Round2",
  name1 = "PingPing",
  name2 = "ParadoX",
  simulator = #0.0,
  t = 41.000000,
  t' = 1
}
#0.0 { className = Simulator, time = 81.000000 }
#0.2 {
  _3D = [["Text", [-2.800000, 6.500000, -4], 1, [0.900000, 0, 0], [1.700000, 0, 0], "ParadoX"], ["Text", [2.800000, 6.500000, -4], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], "PingPing"], ["Text", [-1.500000, 6.500000, -5], 1, [0.600000, 0, 0], [1.700000, 0, 0], 4], ["Text", [4.500000, 6.500000, -5], 1, [0.100000, 0.100000, 0.100000], [1.700000, 0, 0], 13]],
  ball = #0.2.0,
  ballActuator = #0.2.2,
  ballob = #0.2.1,
  bat1 = #0.2.7,
  bat2 = #0.2.8,
  batActuator1 = #0.2.3,
  batActuator2 = #0.2.4,
  className = Game,
  finish = false,
  maxEnergy = 18,
  mode = "Freeze",
  name1 = "ParadoX",
  name2 = "PingPing",
  player1 = #0.2.11,
  player1Score = 4,
  player2 = #0.2.12,
  player2Score = 13,
  referee = #0.2.10,
  serveNumber = 2,
  t = 0.690000,
  t' = 1,
  table = #0.2.9
}
#0.2.0 {
  _3D = ["Sphere", [3.030594, 0.606119, 0.032702], 0.030000, [1, 0, 0], [0, 0, 0]],
  className = Ball,
  k2 = 0.166667,
  k_z = [1, 1, -0.990000],
  mode = "Freeze",
  p = [3.030594, 0.606119, 0.032702],
  p' = [0.000000, 0.000000, 0.000000],
  p'' = [0, 0, 0]
}
#0.2.1 {
  ap = [3.030594, 0.606119, 0.032702],
  className = BallObserver,
  mode = "Estimate0",
  p = [3.030594, 0.606119, 0.032702],
  pp = [3.030594, 0.606119, 0.032702],
  t = 0.010000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000]
}
#0.2.2 {
  action = 0,
  angle = [0.941780, 0.330007, -0.064387],
  className = BallActuator,
  done = false,
  mode = "Initialize",
  v1 = [3.541335, 0.708267, 0.970291],
  v2 = [-4.858327, -2.235039, 1.544552],
  v3 = [-1, 0.200000, 1.200000]
}
#0.2.3 {
  angle = [0.002170, 0.000000, 0.000000],
  className = BatActuator,
  energy = 18.085001,
  energy' = 0.000000,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p'' = [28.551458, 63.545859, 38.541566],
  p1 = [-1.600000, 0, 0.200000]
}
#0.2.4 {
  angle = [0.010000, 0, 0],
  className = BatActuator,
  energy = 18.021159,
  energy' = 0.000000,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p'' = [11.810000, -2.599055, -15.445657],
  p1 = [1.600000, 0, 0.200000]
}
#0.2.5 {
  a = [-12.307692, 0.000000, 0.000000],
  aTemp = [-12.307692, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.000000],
  batAngle' = [0.000000, 0.000000, -0.000000],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [-132.800000, 0.000000, 0.200000],
  desiredBatP' = [-3.200000, 0.000000, 0.000000],
  estimatedBatV = [-519.299408, 0.000000, 0.000000],
  estimatedBatV' = [-12.307692, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 1,
  n1 = 0,
  name = "WiffWaff",
  number = 1,
  reStart = true,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 41.000000,
  t' = 1,
  v = [-3.200000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.2.6 {
  a = [0.000000, 0.000000, 0.000000],
  aTemp = [0.000000, 0.000000, 0.000000],
  ballp = [0, 0, 0],
  ballv = [0, 0, 0],
  batAngle = [0.000000, 0.000000, 0.000000],
  batAngle' = [0.000000, 0.000000, -0.000000],
  batp = [1.600000, 0, 0.200000],
  bounced = false,
  className = WiffWaff,
  count = 0,
  desiredBatP = [1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  mode = "Wait",
  n = 2,
  n1 = 0,
  name = "WiffWaff",
  number = 2,
  reStart = true,
  serve = false,
  startPoint = [1.600000, 0, 0.200000],
  t = 41.000000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
#0.2.7 {
  _3D = ["Box", [-1.650000, 0.000000, 0.200000], [0.300000, 0.300000, 0.300000], [1, 1, 0.100000], [1.248128, -0.000000, -0.900566]],
  angle = [0.002170, 0.000000, 0.000000],
  className = Bat,
  displayAngle = [-1.248128, 0.000000, 0.900566],
  mode = "Rest",
  n = 1,
  p = [-1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [-1.600000, 0, 0.200000]
}
#0.2.8 {
  _3D = ["Box", [1.550000, 0.000000, 0.200000], [0.300000, 0.300000, 0.300000], [1, 1, 0.100000], [-0.000000, 0.101087, -2.088111]],
  angle = [0.010000, 0, 0],
  className = Bat,
  displayAngle = [0.000000, -0.101087, 2.088111],
  mode = "Rest",
  n = 2,
  p = [1.600000, 0.000000, 0.200000],
  p' = [0, 0, 0],
  p1 = [1.600000, 0, 0.200000]
}
#0.2.9 {
  _3D = [["Box", [0, 0, -0.050000], [3, 1.500000, 0.030000], [0.100000, 0.100000, 1.000000], [0, 0, 0]], ["Box", [-1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [-1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, -0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [1.400000, 0.600000, -0.340000], [0.050000, 0.050000, 0.600000], [0.800000, 0.800000, 0.800000], [0, 0, 0]], ["Box", [0, 0, 0.105000], [0.050000, 1.500000, 0.250000], [0.200000, 0.800000, 0.200000], [0, 0, 0]], ["Box", [0, 0, 0], [3, 0.020000, 0.000000], [1, 1, 1], [0, 0, 0]]],
  className = Table
}
#0.2.10 {
  acknowledged = 0,
  bounceTime = 0.140000,
  bounced = "YesIn2",
  checked = false,
  className = Referee,
  lastHit = 2,
  mode = "SendMessage",
  player1Score = 4,
  player2Score = 13,
  reason = "BallOutOfBoundary",
  restart = 1,
  serveNumber = 2,
  status = "Report",
  t = 0.010000,
  t' = 1,
  x = 3.030594,
  x' = 0.000000,
  y = 0.606119,
  z = 0.032702,
  z' = 0.000000
}
#0.2.11 {
  D = [-1.600000, 0, 0],
  Dflag = true,
  Dt = 0,
  a = [0.000000, 0.000000, 0.000000],
  ballp = [3.030594, 0.606119, 0.032702],
  ballv = [0.000000, 0.000000, 0.000000],
  batAngle = [0.002149, 0.000000, 0.000000],
  batAngle' = [-0.002170, 0.000000, 0.000000],
  batp = [-1.600000, 0.000000, 0.200000],
  bounced = false,
  className = ParadoX,
  count = 0,
  desiredBatP = [-1.600000, 0.000000, 0.200000],
  desiredBatP' = [0.000000, 0.000000, 0.000000],
  estimatedBatV = [0.000000, 0.000000, 0.000000],
  estimatedBatV' = [0.000000, 0.000000, 0.000000],
  hit = false,
  hitBack = false,
  mode = "Wait",
  n = 1,
  n1 = 4.711784,
  name = "ParadoX",
  number = 1,
  serve = false,
  startPoint = [-1.600000, 0, 0.200000],
  t = 41.000000,
  t' = 1,
  v = [0.000000, 0.000000, 0.000000],
  v2 = [3.178167, 1.328069, 4.924708],
  v21 = [-2.760913, -1.203126, -3.623638],
  z = -9.174358
}
#0.2.12 {
  a = [0.000000, 0.933364, -0.326516],
  ballp = [3.030594, 0.606119, 0.032702],
  ballv = [0.000000, 0.000000, 0.000000],
  batAngle = [0.010000, 0, 0],
  batAngle' = [-0.010000, 0.000000, 0.000000],
  batp = [1.600000, 0.000000, 0.200000],
  bounced = true,
  className = PingPing,
  count = 0,
  desiredBatP = [1.600000, 13.662824, 0.753424],
  desiredBatP' = [0.000000, 12.122377, -4.182456],
  estimatedBatV = [0.000000, 13.110900, -4.118033],
  estimatedBatV' = [0.000000, 0.933364, -0.326516],
  hit = false,
  mode = "Prepare",
  n = 2,
  n1 = 0,
  name = "PingPing",
  number = 2,
  scount = 0,
  serve = true,
  startPoint = [1.600000, 0, 0.200000],
  t = 41.000000,
  t' = 1,
  v = [0.000000, 12.122377, -4.182456],
  v2 = [0, 0, 0],
  v21 = [0, 0, 0],
  z = 0
}
------------------------------16199
