package restfuljdbi;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jooby.Err;
import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.Request;

import org.jooby.Status;
import org.jooby.jdbi.Jdbi;
import org.jooby.json.Jackson;
import org.skife.jdbi.v2.GeneratedKeys;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Query;
import org.skife.jdbi.v2.util.StringMapper;

public class App extends Jooby {

  {
    /** JSON supports . */
    use(new Jackson());

    /** Create db schema. */
    use(new Jdbi().doWith((dbi, conf) -> {
      try (Handle handle = dbi.open()) {
        //handle.execute(conf.getString("schema"));
      }
    }));
    

    /** lans API. */
    use("/lans")
        /** Get . */
      .get("/:desc", req -> {
      String usr = req.param("usr").value();
      String pwd = req.param("pwd").value();
      System.out.println( " ####"+usr+"#####################"+pwd+"#################### " );
      //***get build json sqls ******
      List<String> sqls = getUserSql(usr,pwd,req);
        try (Handle h = req.require(Handle.class)) {
          List<String> keys = h.createQuery("select distinct INDEX_KEY from bm_lan where GETLANNO(DIST,SECTION,ROAD_NO1,ROAD_NO2) like :desc")
              .bind("desc", "%"+req.param("desc").value()+"%").map(StringMapper.FIRST).list();
          return getAllData(keys,sqls,req);
        }
      });

    use("/aparts")
      .get("/:t_name", req -> {
        String usr = req.param("usr").value();
        String pwd = req.param("pwd").value();

        String sql = getU3Sql(usr,pwd,req);
          try (Handle h = req.require(Handle.class)) {
            List<Map<String, Object>> aparts = h.createQuery(sql)
                .bind("t_name", "%"+req.param("t_name").value()+"%").list();
            return aparts;
          }
        });

    use("/advs")
      .get("/:addr", req -> {
        String usr = req.param("usr").value();
        String pwd = req.param("pwd").value();

        String sql = getU4Sql(usr,pwd,req);
          try (Handle h = req.require(Handle.class)) {
            List<Map<String, Object>> advs = h.createQuery(sql)
                .bind("addr", "%"+req.param("addr").value()+"%").list();
            return advs;
          }
        });

    use("/securities")
      .get("/:d_name", req -> {
        String usr = req.param("usr").value();
        String pwd = req.param("pwd").value();

        String sql = getDecSql(usr,pwd,req);
          try (Handle h = req.require(Handle.class)) {
            List<Map<String, Object>> securities = h.createQuery(sql)
                .bind("d_name", "%"+req.param("d_name").value()+"%").list();
            return securities;
          }
        });
        
    /** bases API. */
    use("/bases")
        /** List pets. */
        .get("/build/:desc",req -> {
        String usr = req.param("usr").value();
        String pwd = req.param("pwd").value();
        System.out.println( " ####"+usr+"#####################"+pwd+"#################### " );
        //***get build json sqls ******
        List<String> sqls = getUserSql(usr,pwd,req);
          try (Handle h = req.require(Handle.class)) {
            List<String> keys = h.createQuery("select distinct INDEX_KEY from bm_base where LICENSE_kind='1' and LICENSE_DESC like :desc")
                            .bind("desc", "%"+req.param("desc").value()+"%").map(StringMapper.FIRST).list();
            return getAllData(keys,sqls,req);
          }
        })
        /** Get a pet by ID. */
        .get("/use/:desc", req -> {
        String usr = req.param("usr").value();
        String pwd = req.param("pwd").value();
        System.out.println( " ####"+usr+"#####################"+pwd+"#################### " );
        //***get build json sqls ******
        List<String> sqls = getUserSql(usr,pwd,req);

          try (Handle h = req.require(Handle.class)) {
            List<String> keys = h.createQuery("select distinct INDEX_KEY from bm_base where LICENSE_kind='3' and LICENSE_DESC like :desc")
                .bind("desc", "%"+req.param("desc").value()+"%").map(StringMapper.FIRST).list();
            return getAllData(keys,sqls,req);      
          }
        });

    /** builds API. */
    use("/builds")
      .get("/addr/:addr",req -> {
        String usr = req.param("usr").value();
        String pwd = req.param("pwd").value();

        System.out.println( " ####"+usr+"#####################"+pwd+"#################### " );
        //***get build json sqls ******
        List<String> sqls = getUserSql(usr,pwd,req);

          try (Handle h = req.require(Handle.class)) {

            List<String> keys = h.createQuery("select distinct INDEX_KEY from bm_p01  where Comb_Addr1(ADDRADR_DESC,ADDRAD1,ADDRAD2,ADDRAD3,ADDRAD4,ADDRAD5,ADDRAD6,ADDRAD6_1,ADDRAD7,ADDRAD7_1,ADDRAD8) like  :addr ")
                .bind("addr", "%"+req.param("addr").value()+"%").map(StringMapper.FIRST).list();
            return getAllData(keys,sqls,req);
          }
        })
        /** Get a  by name .      NAME,BLD_CODE_DESC,BUILDING_NO,CHWANG,DONG,FLOOR,HOUSE  Query<bmP01> q*/
      .get("/:bman", req -> {
          //?name=John&age=99
        String usr = req.param("usr").value();

        String pwd = req.param("pwd").value();
        System.out.println( " ####"+usr+"#####################"+pwd+"#################### " );
        //***get build json sqls ******
        List<String> sqls = getUserSql(usr,pwd,req);
        try (Handle h = req.require(Handle.class)) {
          List<String> keys = h.createQuery("select distinct INDEX_KEY from bm_p01 m where m.NAME like :bman")
                .bind("bman", "%"+req.param("bman").value()+"%").map(StringMapper.FIRST).list();
          return getAllData(keys,sqls,req);  
        }  
      });

  }

  private List<bmBase> getAllData(List<String>keys,List<String>sqls,final Request req) throws Exception {
    List<bmBase> bbs=new ArrayList<bmBase>();
    try (Handle h = req.require(Handle.class)) {  
            ObjectMapper mapper = new ObjectMapper();
            for(String i_key : keys) {
              String tmp = sqls.get(0).trim();
              if (tmp=="No Fields")  
                tmp= "select license_desc from bm_base where index_key = :i_key";//sqls.get(0)

               List<Map<String, Object>> lsBase = h.createQuery(tmp)  
                .bind("i_key", i_key).list();
                
                for (Map<String, Object> m : lsBase)  
                {  
                  bmBase bb = mapper.convertValue(m, bmBase.class); //new bmBase();  
                  //System.out.println(  bb  );  
                  tmp = sqls.get(1).trim();
                  if (!(tmp=="No Fields"))
                  {   
                     List<Map<String, Object>> lsp1 = h.createQuery(sqls.get(1))
                      .bind("i_key", i_key).list();
                      List<bmP01> p1s=new ArrayList<bmP01>();
                      for (Map<String, Object> m1 : lsp1)  
                      {  
                        bmP01 p1 = mapper.convertValue(m1, bmP01.class);//new bmP01();  
                        //System.out.println(  p1  );
                        p1s.add(p1);
                      }  
                      bb.setbmP01s(p1s);
                  }
                  tmp = sqls.get(2).trim();
                  if (!(tmp=="No Fields"))
                  {                     
                     List<Map<String, Object>> lsp2 = h.createQuery(sqls.get(2))
                      .bind("i_key", i_key).list();
                      List<bmP02> p2s=new ArrayList<bmP02>();
                      for (Map<String, Object> m1 : lsp2)  
                      {  
                        bmP02 p2 = mapper.convertValue(m1, bmP02.class);//new bmP02();  
                        //System.out.println(  p2  );
                        p2s.add(p2);
                      }  
                      bb.setbmP02s(p2s);
                  }
                  tmp = sqls.get(3).trim();
                  if (!(tmp=="No Fields"))
                  {                     
                     List<Map<String, Object>> lsp3 = h.createQuery(sqls.get(3))
                      .bind("i_key", i_key).list();
                      List<bmP03> p3s=new ArrayList<bmP03>();
                      for (Map<String, Object> m1 : lsp3)  
                      {  
                        bmP03 p3 = mapper.convertValue(m1, bmP03.class);//new bmP03();  
                        //System.out.println(  p3  );
                        p3s.add(p3);
                      }  
                      bb.setbmP03s(p3s);
                   }
                  tmp = sqls.get(4).trim();
                  if (!(tmp=="No Fields"))
                  {                     
                     List<Map<String, Object>> lsp4 = h.createQuery(sqls.get(4))
                      .bind("i_key", i_key).list();
                      List<bmP04> p4s=new ArrayList<bmP04>();
                      for (Map<String, Object> m1 : lsp4)  
                      {  
                        bmP04 p4 = mapper.convertValue(m1, bmP04.class);//new bmP04();  
                        //System.out.println(  p4  );
                        p4s.add(p4);
                      }  
                      bb.setbmP04s(p4s);
                  }
                  tmp = sqls.get(5).trim();
                  if (!(tmp=="No Fields"))
                  {                     
                     List<Map<String, Object>> lslan = h.createQuery(sqls.get(5))
                      .bind("i_key", i_key).list();
                      List<Lan> lans=new ArrayList<Lan>();
                      for (Map<String, Object> m1 : lslan)  
                      {  
                        Lan lan = mapper.convertValue(m1, Lan.class);//new Lan();  
                        //System.out.println(  lan  );
                        lans.add(lan);
                      }  
                      bb.setLans(lans);
                  }
                  tmp = sqls.get(6).trim();
                  if (!(tmp=="No Fields"))
                  {                     
                     //System.out.println( "sqls.get(5):"+ sqls.get(5)  );
                     List<Map<String, Object>> lstr = h.createQuery(sqls.get(6))
                      .bind("i_key", i_key).list();
                      List<bmStair> trs=new ArrayList<bmStair>();
                      for (Map<String, Object> m1 : lstr)  
                      {  
                        bmStair tr = mapper.convertValue(m1, bmStair.class);//new bmStair();  
                        //System.out.println(  tr  );
                        trs.add(tr);
                      }  
                      bb.setbmStairs(trs);
                    }  
                      bbs.add(bb);
                } // for base 
            } //for key
      return bbs;
    } //try 
  }

  private void Auth(String usr,String pwd,final Request req) throws Exception {
    try (Handle h = req.require(Handle.class)) {  
      String cnt = h.createQuery("select count(*) from  bwuser where usrid = :usr and passwd = :pwd ")
                    .bind("usr", usr)
                    .bind("pwd", pwd)
                    .map(StringMapper.FIRST)
                    .first();   

      if ( Integer.parseInt(cnt.trim()) == 0 ) 
      {
        String data ="帳號密碼未經授權";
        //rsp.status(200)
        //.type("text/plain")
        //.send(data); 
        //return  data;
        throw new Err(Status.UNAUTHORIZED);
      }         
    }  
  }  

  private String getU3Sql(String usr,String pwd,final Request req) throws Exception {
     Auth(usr,pwd,req);
    try (Handle h = req.require(Handle.class)) { 
      String role =  h.createQuery("select roleid from  bwuser where usrid = :usr and passwd = :pwd ")
                      .bind("usr", usr)
                      .bind("pwd", pwd)
                      .map(StringMapper.FIRST)
                      .first();   

      String sqlu3 = "";
      if (role != null && !role.isEmpty())
      {
        //BASE
        List<String>lsbs = h.createQuery("SELECT distinct f.FIELD FROM BWFIELDSRIGHT r, BWFIELDS f  where r.FID = f.FID AND f.TBL='UMVW_4G_U3'  AND (r.ROLEID= :role OR r.ROLEID= :usr )")
                            .bind("role", role)
                            .bind("usr", usr)
                            .map(StringMapper.FIRST).list();

        for (int i = 0; i < lsbs.size(); i++) {
             sqlu3 =  lsbs.get(i)+","+sqlu3;
            }
        if (sqlu3.length()==0) 
        {   
          sqlu3="No Fields";
          
          throw new Err(Status.NON_AUTHORITATIVE_INFORMATION);
        }  
        else
        {  
          sqlu3 = sqlu3.substring(0,sqlu3.length()-1 );    
          sqlu3="SELECT "+ sqlu3 +" FROM UMVW_4G_U3 WHERE T3111NAME LIKE  :t_name";
          System.out.println( sqlu3 );  
        }
      }  
    return sqlu3;
    }//try
  }
    
  private String getU4Sql(String usr,String pwd,final Request req) throws Exception {
    Auth(usr,pwd,req);
    String sqlu3 = "";
    try (Handle h = req.require(Handle.class)) { 
      String role =  h.createQuery("select roleid from  bwuser where usrid = :usr and passwd = :pwd ")
                      .bind("usr", usr)
                      .bind("pwd", pwd)
                      .map(StringMapper.FIRST)
                      .first();   

      
      if (role != null && !role.isEmpty())
      {
        //BASE
        List<String>lsbs = h.createQuery("SELECT distinct f.FIELD FROM BWFIELDSRIGHT r, BWFIELDS f  where r.FID = f.FID AND f.TBL='UMVW_4G_U4'  AND (r.ROLEID= :role OR r.ROLEID= :usr )")
                            .bind("role", role)
                            .bind("usr", usr)
                            .map(StringMapper.FIRST).list();

        for (int i = 0; i < lsbs.size(); i++) {
             sqlu3 =  lsbs.get(i)+","+sqlu3;
            }
        if (sqlu3.length()==0) 
        {   
          sqlu3="No Fields";
          
          throw new Err(Status.NON_AUTHORITATIVE_INFORMATION);
        }  
        else
        {  
        sqlu3 = sqlu3.substring(0,sqlu3.length()-1 );    
        sqlu3="SELECT "+ sqlu3 +" FROM UMVW_4G_U4 WHERE ADDRESS LIKE :addr";
        System.out.println( sqlu3 );
        }  
      }  
    } //try 
    return sqlu3;
  }

  private String getDecSql(String usr,String pwd,final Request req) throws Exception {
     Auth(usr,pwd,req);
     String sqlu3 = "";
    try (Handle h = req.require(Handle.class)) {  
      String role =  h.createQuery("select roleid from  bwuser where usrid = :usr and passwd = :pwd ")
                      .bind("usr", usr)
                      .bind("pwd", pwd)
                      .map(StringMapper.FIRST)
                      .first();   

      
      if (role != null && !role.isEmpty())
      {
        //
        List<String>lsbs = h.createQuery("SELECT distinct f.FIELD FROM BWFIELDSRIGHT r, BWFIELDS f  where r.FID = f.FID AND f.TBL='DECBM'  AND (r.ROLEID= :role OR r.ROLEID= :usr )")
                            .bind("role", role)
                            .bind("usr", usr)
                            .map(StringMapper.FIRST).list();

        for (int i = 0; i < lsbs.size(); i++) {
             sqlu3 =  lsbs.get(i)+","+sqlu3;
            }
        if (sqlu3.length()==0) 
        {   
          sqlu3="No Fields";
          
          throw new Err(Status.NON_AUTHORITATIVE_INFORMATION);
        }  
        else
        {  
        sqlu3 = sqlu3.substring(0,sqlu3.length()-1 );    
        sqlu3="SELECT "+ sqlu3 +" FROM DECBM WHERE DECNAM LIKE :d_name";
        System.out.println( sqlu3 );  
        }
      }  
    } //try 
    return sqlu3;
  }


  private List<String> getUserSql(String usr,String pwd,final Request req) throws Exception {
    List<String> sqls=new ArrayList<String>();
    Auth(usr,pwd,req);
    try (Handle h = req.require(Handle.class)) { 
      String role =  h.createQuery("select roleid from  bwuser where usrid = :usr and passwd = :pwd ")
                      .bind("usr", usr)
                      .bind("pwd", pwd)
                      .map(StringMapper.FIRST)
                      .first();   

        String sqlBase = "";
        String sqlP01 = "";
        String sqlP02 = "";
        String sqlP03 = "";
        String sqlP04 = "";
        String sqlLan = "";
        String sqlStair = "";

        if (role != null && !role.isEmpty())
        {
          //BASE
          String sSel = "SELECT distinct f.FIELD FROM BWFIELDSRIGHT r, BWFIELDS f  where r.FID = f.FID AND f.TBL=";
          String sWhr = "  AND (r.ROLEID= :role OR r.ROLEID= :usr )";
          List<String>lsbs = h.createQuery(sSel+"'BM_BASE'"+ sWhr)
                              .bind("role", role)
                              .bind("usr", usr)
                              .map(StringMapper.FIRST).list();

          for (int i = 0; i < lsbs.size(); i++) {
               sqlBase =  lsbs.get(i)+","+sqlBase;
          }
          if (sqlBase.length()==0) 
          {   
            sqlBase="No Fields";
          }  
          else
          {  
            sqlBase = sqlBase.substring(0,sqlBase.length()-1 );    
            sqlBase="SELECT "+ sqlBase +" FROM bm_base WHERE INDEX_KEY = :i_key";
          }
          //System.out.println( sqlBase );  
          //P01
          List<String> ls1 = h.createQuery(sSel+"'BM_P01'"+ sWhr)
                              .bind("role", role)
                              .bind("usr", usr)
                              .map(StringMapper.FIRST).list();

          for (int i = 0; i < ls1.size(); i++) {
            String tmp = ls1.get(i);  
            //System.out.println( tmp );    
            if ( tmp.trim().equals("ADDRADR") ) //==
               sqlP01 =  " Comb_Addr1(ADDRADR_DESC,ADDRAD1,ADDRAD2,ADDRAD3,ADDRAD4,ADDRAD5,ADDRAD6,ADDRAD6_1,ADDRAD7,ADDRAD7_1,ADDRAD8) addradr ,"+sqlP01;
            else  if ( tmp.trim().equals("BUILDING_NO"))
               sqlP01 =  " Comb_cdfh(BUILDING_NO, CHWANG, DONG, FLOOR, HOUSE)  building_no,"+sqlP01;
            else  
               sqlP01 =  ls1.get(i)+","+sqlP01;
          }
          if (sqlP01.length()==0) 
          {   
            sqlP01="No Fields";
          }  
          else
          {  
            sqlP01 = sqlP01.substring(0,sqlP01.length()-1 );    
            sqlP01="SELECT "+ sqlP01 +" FROM bm_P01 WHERE INDEX_KEY = :i_key";
          }  
          //System.out.println( sqlP01 );  
          //P02   
          List<String> ls2 = h.createQuery(sSel+"'BM_P02'"+sWhr)
                              .bind("role", role)
                              .bind("usr", usr)
                              .map(StringMapper.FIRST).list();

          for (int i = 0; i < ls2.size(); i++) {
               sqlP02 =  ls2.get(i)+","+sqlP02;
              }
          if (sqlP02.length()==0) 
          {   
            sqlP02="No Fields";
          }  
          else
          {  
            sqlP02 = sqlP02.substring(0,sqlP02.length()-1 );    
            sqlP02="SELECT "+ sqlP02 +" FROM bm_P02 WHERE INDEX_KEY = :i_key";
          }  
          //System.out.println( sqlP02 );  
          //P03 
          List<String> ls3 = h.createQuery(sSel+"'BM_P03'"+sWhr)
          .bind("role", role)
          .bind("usr", usr)
          .map(StringMapper.FIRST).list();

          for (int i = 0; i < ls3.size(); i++) {
               sqlP03 =  ls3.get(i)+","+sqlP03;
          }
          if (sqlP03.length()==0) 
          {   
            sqlP03="No Fields";
          }  
          else
          {  
            sqlP03 = sqlP03.substring(0,sqlP03.length()-1 );    
            sqlP03="SELECT "+ sqlP03 +" FROM bm_P03 WHERE INDEX_KEY = :i_key";
          }  
          //System.out.println( sqlP03 );  

          //P04 
          List<String> ls4 = h.createQuery(sSel+"'BM_P04'"+sWhr)
          .bind("role", role)
          .bind("usr", usr)
          .map(StringMapper.FIRST).list();

          for (int i = 0; i < ls4.size(); i++) {
               sqlP04 =  ls4.get(i)+","+sqlP04;
          }
          if (sqlP04.length()==0) 
          {   
            sqlP04="No Fields";
          }  
          else
          {  
            sqlP04 = sqlP04.substring(0,sqlP04.length()-1 );    
            sqlP04="SELECT "+ sqlP04 +" FROM bm_P04 WHERE INDEX_KEY = :i_key";
          }  
          //System.out.println( sqlP04 );  

          //Lan 
          List<String> lsn = h.createQuery(sSel+"'BM_LAN'"+sWhr)
          .bind("role", role)
          .bind("usr", usr)
          .map(StringMapper.FIRST).list();

          for (int i = 0; i < lsn.size(); i++) {
            String tmp = lsn.get(i);
            //System.out.println( tmp );    
            if ( tmp.trim().equals("LAN"))
               sqlLan =  " GETLANNO(DIST,SECTION,ROAD_NO1,ROAD_NO2) lan,"+sqlLan;
            else  
               sqlLan =  lsn.get(i)+","+sqlLan;
          }
          if (sqlLan.length()==0) 
          {   
            sqlLan="No Fields";
          }  
          else
          {  
            sqlLan = sqlLan.substring(0,sqlLan.length()-1 );    
            sqlLan="SELECT "+ sqlLan +" FROM bm_LAN WHERE INDEX_KEY = :i_key";
          }
          //System.out.println( sqlLan );   
          //Stair 
          List<String> lsr = h.createQuery(sSel+"'BM_STAIR'"+sWhr)
          .bind("role", role)
          .bind("usr", usr)
          .map(StringMapper.FIRST).list();

          for (int i = 0; i < lsr.size(); i++) {
               sqlStair =  lsr.get(i)+","+sqlStair;
          }
          if (sqlStair.length()==0) 
          {   
            sqlStair="No Fields";
          }  
          else
          {  
            sqlStair = sqlStair.substring(0,sqlStair.length()-1 );        
            sqlStair="SELECT "+ sqlStair +" FROM bm_STAIR WHERE INDEX_KEY = :i_key";
          }
          //System.out.println(  sqlStair  );   

        } else
        {
          String data ="帳號密碼未設角色群組";
          //return data;
           throw new Err(Status.NOT_ACCEPTABLE);
        }

        sqls.add(sqlBase);
        sqls.add(sqlP01);
        sqls.add(sqlP02);
        sqls.add(sqlP03);
        sqls.add(sqlP04);
        sqls.add(sqlLan);
        sqls.add(sqlStair);      
    } //try    
        return sqls;
  }

  public static void main(final String[] args) throws Exception {
    new App().start(args);
  }

}
