public class Main {

  private String string;

  public static void main(String[] args) throws Exception {


  }


  public static void main1(String[] args) throws Exception {
    final String path = "/Users/spyne/data/connections/1/part-00000.avro";

//    LogManager.getRootLogger().setLevel(Level.DEBUG);
//    final ConsoleAppender consoleAppender = new ConsoleAppender();
//    LogManager.getRootLogger().addAppender(consoleAppender);
//    LogManager.getLogger("hive").setLevel(Level.DEBUG);
//    LogManager.getLogger("hive").addAppender(consoleAppender);
//    Main main = new Main();
//    main.testHmsClient();
//
  }
//
//  public void testHmsClient() throws Exception {
//    HiveConf conf = new HiveConf();
////    conf.set("fs.defaultFS", "hdfs://lva1-spadesnn01.grid.linkedin.com:9000");
//    conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
//    conf.setVar(HiveConf.ConfVars.METASTOREURIS, "thrift://lva1-spadeshcat01.grid.linkedin.com:7552");
//    conf.setBoolVar(HiveConf.ConfVars.METASTORE_USE_THRIFT_SASL, true);
//    conf.setVar(HiveConf.ConfVars.METASTORE_KERBEROS_PRINCIPAL, "hcat/_HOST@GRID.LINKEDIN.COM");
////    conf.setBoolVar(HiveConf.ConfVars.HIVE_AUTHORIZATION_ENABLED, true);
////    conf.setVar(HiveConf.ConfVars.HIVE_AUTHORIZATION_MANAGER,
////        "org.apache.hadoop.hive.ql.security.authorization.StorageBasedAuthorizationProvider");
//    IMetaStoreClient metastoreClient = null;
//    metastoreClient = new HiveMetaStoreClient(conf);
////    metastoreClient.getDatabases("default");
//
//    org.apache.hadoop.hive.ql.metadata.Table t1 = new org.apache.hadoop.hive.ql.metadata.Table(metastoreClient.getTable("default", "hivetest"));
//    org.apache.hadoop.hive.ql.metadata.Table t2 = new org.apache.hadoop.hive.ql.metadata.Table(metastoreClient.getTable("u_spyne", "t1"));
//
//    HadoopDefaultAuthenticator authenticator = new HadoopDefaultAuthenticator();
//    authenticator.setConf(conf);
//
//    StorageBasedAuthorizationProvider p = new StorageBasedAuthorizationProvider();
//    p.setConf(conf);
//    p.setAuthenticator(authenticator);
//    p.init(conf);
//
//    Privilege read[] = new Privilege[]{ new Privilege(PrivilegeType.SELECT) };
//    Privilege write[] = new Privilege[]{  };
//    p.authorize(t2, read, write);
//    p.authorize(t1, read, write);
//  }
}
