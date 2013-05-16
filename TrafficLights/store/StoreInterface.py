'''
Created on Mar 8, 2013

@author: samehrotra
'''

# A data access interface
# To Interface between CAL Report server and Data Storage for enabling TrafficLights Analysis
class StoreInterface:
    
    #Get data represented as a hierarchy of components  
    def GetComponentHierarchy( self ):
        pass
    
    #Get Threshold monitors for components to decide and assign health values
    def GetComponentMonitors( self ):
        pass  

    #Connect to database
    def Connect( self ):
        pass    
    
    #Disconnect from database, return the object which can be tested for successful attempt    
    def Disconnect( self ):
        pass