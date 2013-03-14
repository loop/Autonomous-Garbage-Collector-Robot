include "exploresensor.inc"

# You may edit the definition of robot_ranger.  
# You can change the position and direction of the exploresensors,
# add additional exploresensors or remove exploresensors.  Your 
# robot may possess up to 16 exploresensors (not more).  Do not
# remove the include line.

define robot_ranger ranger 
(
   exploresensor ( pose [ 0.000 -0.012 0.1 337] )
   exploresensor ( pose [ 0.003 -0.009 0.1 342] )
   exploresensor ( pose [ 0.006 -0.006 0.1 347] )
   exploresensor ( pose [ 0.009 -0.003 0.1 352] )
   exploresensor ( pose [ 0.012  0.000 0.1   0] )
   exploresensor ( pose [ 0.009  0.003 0.1   8] )
   exploresensor ( pose [ 0.006  0.006 0.1  13] )
   exploresensor ( pose [ 0.003  0.009 0.1  18] )
   exploresensor ( pose [ 0.000  0.012 0.1  23] ) 
   
   exploresensor ( pose [-0.125  0.000 0.1 180] )
   exploresensor ( pose [ 0.000  0.125 0.1  90] )
   exploresensor ( pose [ 0.000 -0.125 0.1 270] )
)
