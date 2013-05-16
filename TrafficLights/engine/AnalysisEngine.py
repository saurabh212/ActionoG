'''
Created on Mar 12, 2013

@author: samehrotra
'''
from SuperCALScrapperEngine import SuperCALScrapperEngine
from SuperCALDataTypes import SuperCALMessage, SITE_MAP, gMonths, ESiteSLA

#Class which assimilates Domain - component -hierarchy data, threshold data from CAL, 
#and live C.A.L. reports. And churns this data into health status colors. 
class AnalysisEngine:
    
    def __init__( self ):
        pass
        
    def Display( self ):
        
        calObj = SuperCALScrapperEngine()
        calObj.LoadReports('riskpaymentserv', 20, 12, 3, 2013 )
        obj = calObj.GetTrans( ESiteSLA, 'ASYN_DISPATCH', 'dblogentryservice')
        #obj = calObj.GetTransList( SITE_MAP[ ESiteSLA ] )
        
        print "My Object has %d  %d  %d  %d " % (obj.iTotlCount, obj.iFailCount, obj.iAvergTime, obj.iMaximTime)
        return obj
    
if __name__ == '__main__':
    new = AnalysisEngine()
    print new.Display()
