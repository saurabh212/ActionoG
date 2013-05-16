'''
Created on Mar 8, 2013

@author: samehrotra
'''
from SQLiteStoreImpl import SQLiteStoreImpl

# StoreFactory module enables Factory Production for each Storage type.
# Select database storage type
def GetStore( _storeType ):
    # case: SQLite version is to be created
    if _storeType == 'SQLITE':
        return SQLiteStoreImpl()
    return None

# Test Case
if __name__ == '__main__':

    store = GetStore( 'SQLITE' )
    store.Connect()
    store.GetComponentHierarchy()
    store.GetComponentMonitors()
    store.Disconnect()
