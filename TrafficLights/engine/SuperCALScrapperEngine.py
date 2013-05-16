import urllib, zlib, gc, re, xml.dom.minidom
from SuperCALDataTypes import SuperCALMessage, SITE_MAP, gMonths

FILE_SIZE_PATTERN = 'total size = ([0-9]+)K'


def TimeToDir( aHour, aDate, aMonth, aYear ):
    buff = ''
    if aHour == -1:
        buff = '%d/%02d-%s/%02d/report' % ( aYear, aMonth, gMonths[aMonth], aDate )
    else:
        buff = '%d/%02d-%s/%02d/%02d:00:00/report' % ( aYear, aMonth, gMonths[aMonth], aDate, aHour )
    return buff

##### XML Parsing Stuff #######
def GetCountFromName( aType, aName, aNode ):
    for type_xml in aNode.getElementsByTagName( 'msgType' ):
        if type_xml.getAttribute( 'name' ) == aType:
            for name_xml in type_xml.childNodes:
                if name_xml.getAttribute( 'name' ).find( aName ) == 0:
                    count = float( name_xml.getAttribute( 'name' ).replace( aName, '' ) )
                    return count
    return None

def GetTypeNameXML( aType, aName, aNode ):
    for type_xml in aNode.getElementsByTagName( 'msgType' ):
        if type_xml.getAttribute( 'name' ) == aType:
            for name_xml in type_xml.childNodes:
                if name_xml.getAttribute( 'name' ) == aName:
                    return name_xml
    return None

def GetText( aNodeList ):
    rc = []
    for node in aNodeList:
        if node.nodeType == node.TEXT_NODE:
            rc.append( node.data )
    return ''.join( rc )

def GetMetric( aNode, aMetric ):
    elements = aNode.getElementsByTagName( aMetric )
    if len( elements ) == 0:
        return 0.0
    metric = GetText( elements[0].childNodes )
    if metric != '':
        return float( metric )
    return 0.0

def GetNamesInType( aType, aNode ):
    names = []
    if not aNode:
        return names
    for type_xml in aNode.getElementsByTagName( 'msgType' ):
        if type_xml.getAttribute( 'name' ) == aType:
            for name_xml in type_xml.childNodes:
                names.append( name_xml.getAttribute( 'name' ) )
    return names

def GetTypeList( aNode ):
    types = []
    if not aNode:
        return types
    for type_xml in aNode.getElementsByTagName( 'msgType' ):
        types.append( type_xml.getAttribute( 'name' ) )
    return types


class SuperCALSiteReport:

    def __init__( self, aLocation ):
        self.iRptLoc = aLocation
        self.iTxnXML = None
        self.iEvtXML = None
        self.iHBtCSV = None

    def GetReports( self ):
        self.iTxnXML = self.GetXML( self.iRptLoc + 'SuperCAL-Transaction.xml.gz' )
        self.iEvtXML = self.GetXML( self.iRptLoc + 'SuperCAL-Event.xml.gz' )
        #self.iHBtCSV = self.GetCSV( self.iRptLoc + 'supercal-heartbeat.csv.gz' )


    def GetXML( self, aXMLRemoteFile ):
        dcompr = zlib.decompressobj( 16 + zlib.MAX_WBITS )
        resXML = None
        try:
            xmlFiH = urllib.urlopen( aXMLRemoteFile )
            if xmlFiH.getcode() == 200:
                resXML = xml.dom.minidom.parseString( dcompr.decompress( xmlFiH.read() ).replace( '\n', '' ) )
            xmlFiH.close()
        except:
            pass
        dcompr = None
        return resXML

    def GetCSV( self, aCSVRemoteFile ):
        dcompr = zlib.decompressobj( 16 + zlib.MAX_WBITS )
        resCSV = None
        try:
            xmlFiH = urllib.urlopen( aCSVRemoteFile )
            if xmlFiH.getcode() == 200:
                rmtLns = dcompr.decompress( xmlFiH.read() ).split( '\n' )
                rmtLns.pop( 0 )
                resCSV = []
                for line in rmtLns:
                    resCSV.append( tuple( line.split( ',' ) ) )
        except:
            pass
        dcompr = None
        return resCSV

    def GetTrans( self, aType, aName ):
        if self.iTxnXML:
            return self.GetStat( aType, aName, self.iTxnXML )
        return None

    def GetEvent( self, aType, aName ):
        if self.iEvtXML:
            return self.GetStat( aType, aName, self.iEvtXML )
        return None

    def GetStat( self, aType, aName, aXML ):
        calMsg = SuperCALMessage()
        if aName[-1] == '*':
            calMsg.iTotlCount = GetCountFromName( aType, aName[:-1], aXML )
        else:
            msgXML = GetTypeNameXML( aType, aName, aXML )
            if msgXML:
                calMsg.iTotlCount = GetMetric( msgXML, 'totalCount' )
                calMsg.iFailCount = GetMetric( msgXML, 'failureCount' )
                calMsg.iAvergTime = GetMetric( msgXML, 'avgDuration' )
                calMsg.iMaximTime = GetMetric( msgXML, 'maxDuration' )
                calMsg.iAvgFlTime = GetMetric( msgXML, 'avgFailureDuration' )
        return calMsg

    def GetTxnNames( self, aType ):
        return GetNamesInType( aType, self.iTxnXML )

    def GetEvtNames( self, aType ):
        return GetNamesInType( aType, self.iEvtXML )

    def GetTransList( self ):
        return GetTypeList( self.iTxnXML )

    def GetEventList( self ):
        return GetTypeList( self.iEvtXML )

    def GetHBeat( self, aMachine, aHBeat, aStat ):
        for hbmsg in self.iHBtCSV:
            if len( hbmsg ) > 9 and hbmsg[2] == aMachine and hbmsg[3] == aHBeat and hbmsg[4] == aStat:
                retObj = SuperCALMessage()
                retObj.iTotlCount = float( hbmsg[6] )
                retObj.iHtBtCount = float( hbmsg[5] )
                retObj.iMaximTime = float( hbmsg[8] )
                retObj.iAvergTime = float( hbmsg[9] )
                retObj.iFailCount = 0.0
                retObj.iAvgFlTime = 0.0
                return retObj
        return None

class SuperCALScrapperEngine:

    iReportCache = {}

    def LoadReports( self, aPool, aHour, aDate, aMonth, aYear ):
        gc.collect()
        self.iReportCache = {}
        url_mid = TimeToDir( aHour, aDate, aMonth, aYear ) + '/' + aPool + '/'
        for site in SITE_MAP.keys():
            reportIndx = SITE_MAP[site] + url_mid
            self.iReportCache[site] = SuperCALSiteReport( reportIndx )
        for worker in self.iReportCache.values():
            worker.GetReports()

    # Return a transaction metric
    def GetTrans( self, aSite, aType, aName ):
        if aSite in SITE_MAP.keys():
            return self.iReportCache[aSite].GetTrans( aType, aName )
        return None

    # Return a event metric
    def GetEvent( self, aSite, aType, aName ):
        if aSite in SITE_MAP.keys():
            return self.iReportCache[aSite].GetEvent( aType, aName )
        return None

    # List of names found under a type in Transaction
    def GetTxnNames( self, aSite, aType ):
        if aSite in SITE_MAP.keys():
            return self.iReportCache[aSite].GetTxnNames( aType )
        return []

    # List of names found under a type in Event
    def GetEvtNames( self, aSite, aType ):
        if aSite in SITE_MAP.keys():
            return self.iReportCache[aSite].GetEvtNames( aType )
        return []

    # List of transaction types
    def GetTransList( self, aSite ):
        if aSite in SITE_MAP.keys():
            return self.iReportCache[aSite].GetTransList()
        return []

    # List of event types
    def GetEventList( self, aSite ):
        if aSite in SITE_MAP.keys():
            return self.iReportCache[aSite].GetEventList()
        return []

    # Not tested yet :-)
    def GetHBeat( self, aSite, aMachine, aHBeat, aStat ):
        if aSite in SITE_MAP.keys():
            return self.iReportCache[aSite].GetHBeat( aMachine, aHBeat, aStat )
        return None

    def GetLogSize( self, aSite, aPool, aHour, aDate, aMonth, aYear ):
        if aSite not in SITE_MAP.keys():
            return 0
        url_mid = '%s/%s/' % ( TimeToDir( aHour, aDate, aMonth, aYear ), aPool )
        home_pg = SITE_MAP[aSite] + url_mid
        try:
            urlFiH = urllib.urlopen( home_pg )
            if urlFiH.getcode() == 200:
                pageContent = urlFiH.readlines()
                urlFiH.close()
                for line in pageContent:
                    mObj = re.search( FILE_SIZE_PATTERN, line )
                    if mObj:
                        return int( mObj.group( 1 ) ) * 1024
        except:
            return 0



if __name__ == '__main__':
    from SuperCALDataTypes import ESiteSLA
    impl = SuperCALScrapperEngine()
    impl.LoadReports('riskpaymentserv', 20, 11, 3, 2013 )
    obj = impl.GetTrans( ESiteSLA, 'ASYN_DISPATCH', 'dblogentryservice')
    print obj

