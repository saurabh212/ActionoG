'''
Created on Mar 8, 2013

@author: samehrotra
'''
from StoreInterface import StoreInterface
import sqlite3


# Implements the StoreInterface specific to SQLite Database type
class SQLiteStoreImpl( StoreInterface ):

    # Initialize the variables
    def __init__( self, _dbFilePath ):
        self.iDBFilePath = _dbFilePath
        self.iConnection = None

    # Get data represented as a hierarchy of components
    def GetComponentHierarchy( self ):
        pass

    # Get Threshold monitors for components to decide and assign health values
    def GetComponentMonitors( self ):
        pass

    # Connect to database
    def Connect( self ):
        try:
            self.iConnection = sqlite3.connect( self.iDBFilePath )
            return ( True, None )
        except sqlite3.Error, e:
            return ( False, e.args[0] )

    # Disconnect from database, return the object which can be tested for successful attempt
    def Disconnect( self ):
        if self.iConnection:
            self.iConnection.close()

# Test Case
if __name__ == '__main__':
    print 'running as main module'
    test_db = 'test/TrafficLights.db'
    sqlite = SQLiteStoreImpl( test_db )
    sqlite.Connect()
    sqlite.Disconnect()
