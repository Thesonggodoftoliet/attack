package com.littlepants.attack.attackplus.utils;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.*;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


/**
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @date 2022/12/27
 * @description
 */
public class GraphDBUtil {
    private static final Logger graphdbLogger =  LoggerFactory.getLogger(GraphDBUtil.class);
    private static OrientDB orientDB;
    private static OrientDBConfigBuilder poolConfig;
    private static ODatabasePool pool;
    @Value("${orientdb.pool.min}")
    private static int poolMin;
    @Value("${orientdb.pool.max}")
    private static int poolMax;
    @Value("${orientdb.url}")
    private static String url;
    @Value("${orientdb.server.user}")
    private static String serverUser;
    @Value("${orientdb.server.password}")
    private static String serverPassword;
    @Value("${orientdb.database.user}")
    private static String databaseUser;
    @Value("${orientdb.database.password}")
    private static String databasePassword;

    public static void connect(String databaseName){
        orientDB = new OrientDB(url,serverUser,serverPassword, OrientDBConfig.defaultConfig());
        poolConfig = OrientDBConfig.builder();
        poolConfig.addConfig(OGlobalConfiguration.DB_POOL_MIN,poolMin);
        poolConfig.addConfig(OGlobalConfiguration.DB_POOL_MAX,poolMax);
        pool = new ODatabasePool(orientDB,databaseName,databaseUser,databasePassword,poolConfig.build());
        graphdbLogger.info("连接图形数据库成功! 地址:"+url+" 数据库:"+databaseName);
    }

    public static void close(){
        pool.close();
        orientDB.close();
        graphdbLogger.info("关闭数据库");
    }

    public static void execute(){

    }

    /**
     *
     * @param databaseName 数据库名称
     */
    public static void createDatabase(String databaseName){
        orientDB = new OrientDB(url,serverUser,serverPassword,OrientDBConfig.defaultConfig());
        orientDB.create(databaseName,ODatabaseType.PLOCAL);
        orientDB.close();
    }

    /**
     *
     * @param databaseName 数据库名称
     */
    public static void dropDatabase(String databaseName){
        orientDB = new OrientDB(url,serverUser,serverPassword,OrientDBConfig.defaultConfig());
        orientDB.drop(databaseName);
        orientDB.close();
    }

    /**
     *
     * @param databaseName 数据库名称
     * @param clazzName 创建的Class的全名，eg：com.attack.attackplus.entity.graph.Asset
     */
    public static void createSchema(String databaseName, String clazzName) throws ClassNotFoundException{
        connect(databaseName);
        try (ODatabaseSession db = pool.acquire()){
            Class clazz = Class.forName(clazzName);
            Field[] fields = clazz.getDeclaredFields();
            OClass oClass = db.getMetadata().getSchema().createClass(clazz.getSimpleName());
            for (Field field:fields){
                OType oType = OType.getTypeByClass(field.getType());
                oClass.createProperty(field.getName(),oType);
            }
        }finally {
            close();
        }
    }

    /**
     *
     * @param databaseName 选择的数据库名
     * @param clazzName 类的全限定名
     * @return
     */
    public static OClass getClass(String databaseName,String clazzName){
        connect(databaseName);
        try (ODatabaseSession db = pool.acquire()){
            int index = clazzName.lastIndexOf('.');
            OClass oClass = db.getClass(clazzName.substring(index+1));
            return oClass;
        }finally {
            close();
        }
    }

    /**
     *
     * @param databaseName 选择的数据库名
     * @param clazzName 类的全限定名
     */
    public static void dropClass(String databaseName,String clazzName){
        connect(databaseName);
        try (ODatabaseSession databaseSession = pool.acquire()){
            int index = clazzName.lastIndexOf('.');
            String name = clazzName.substring(index+1);
            databaseSession.getMetadata().getSchema().dropClass(name);
        }finally {
            close();
        }
    }

    /**
     *
     * @param databaseName 选择的数据库名
     * @param clazzName 类的全限定名
     */
    public static void createVertexClass(String databaseName,String clazzName)throws ClassNotFoundException{
        connect(databaseName);
        try (ODatabaseSession db = pool.acquire()){
            Class aclass = Class.forName(clazzName);
            Field[] fields = aclass.getDeclaredFields();
            OClass oClass = db.createVertexClass(aclass.getSimpleName());
            for (Field field:fields){
                OType oType = OType.getTypeByClass(field.getType());
                oClass.createProperty(field.getName(),oType);
            }
        }finally {
            close();
        }
    }

    public static void dropVertexClass(String databaseName,String clazzName){
        dropClass(databaseName,clazzName);
    }

//    public static void saveVertexData(Asset asset){
//        //目前来说，该系统数据库的顶点仅为资产，故在接口处指定为资产
//    }

    /**
     *
     * @param databaseName 数据库名
     * @param data 数据
     * @param <T> 数据类型
     * @throws IllegalAccessException
     */
    public static <T>void saveVertexData(String databaseName, T data)throws IllegalAccessException{
        connect(databaseName);
        try (ODatabaseSession db = pool.acquire()){
            OVertex oVertex = db.newVertex(data.getClass().getSimpleName());
            Field[] fields = data.getClass().getDeclaredFields();
            for (Field field:fields){
                field.setAccessible(true);
                int mod = field.getModifiers();
                if (Modifier.isFinal(mod)||Modifier.isStatic(mod)) {
                }
                else if (field.getType()== List.class && field.get(data)!=null){
                    OType oType = OType.getTypeByClass(field.getClass());// List.class
                    List dataList = (List) field.get(data);// data 原有的List

                    //List里面是不是自定义类
                    //类里面不能再含别的未注册类
                    if (ClassUtil.isCustomer(dataList.get(0).getClass())) {
                        List<Object> newList = new ArrayList<>();
                        for (Object object : dataList) {
                            OVertex oVertex1 = db.newVertex(object.getClass().getSimpleName());
                            for (Field field1 : object.getClass().getDeclaredFields()) {
                                OType oType1 = OType.getTypeByClass(field1.getClass());
                                oVertex1.setProperty(field1.getName(), field1.get(object), oType1);
                            }
                            newList.add(oVertex1);
                        }
                        oVertex.setProperty(field.getName(),newList,oType);
                    }else
                        oVertex.setProperty(field.getName(),dataList,oType);
                }else if (field.getType()== Map.class && field.get(data)!=null){
                    OType oType = OType.getTypeByClass(Map.class);
                    Map<String,Object> dataMap = (Map<String,Object>) field.get(data);
                    for (String key:dataMap.keySet()){
                        Object obj = dataMap.get(key);
                        if (ClassUtil.isCustomer(obj.getClass())){
                            OVertex oVertex1 = db.newVertex(obj.getClass().getSimpleName());
                            for (Field field1:obj.getClass().getDeclaredFields()){
                                OType oType1 = OType.getTypeByClass(field1.getClass());
                                oVertex1.setProperty(field1.getName(),field1.get(obj),oType1);
                            }
                            dataMap.put(key,oVertex1);
                        }
                    }
                    oVertex.setProperty(field.getName(),dataMap,oType);
                }else {
                    OType oType = OType.getTypeByClass(field.getClass());
                    oVertex.setProperty(field.getName(),field.get(data),oType);
                }
            }
            graphdbLogger.info("当前存储数据为:"+oVertex);
            oVertex.save();
        }finally {
            close();
        }
    }

    /**
     *
     * @param databaseName 选择数据库的名字
     * @param clazzName 创建的类的名字
     * @param properties String类型的属性名列表
     */
    public static void createEdgeClass(String databaseName, String clazzName, List<String> properties){
        connect(databaseName);
        try (ODatabaseSession db = pool.acquire()){
            OClass oClass = db.createEdgeClass(clazzName);
            for (String propertyName: properties)
                oClass.createProperty(propertyName,OType.STRING);
        }finally {
            close();
        }
    }

    public static void dropEdgeClass(String databaseName,String clazzName){
        dropClass(databaseName,clazzName);
    }


    public static  void addEdge(String databaseName, ORID a, ORID b, OClass edge){
        connect(databaseName);
        try (ODatabaseSession db = pool.acquire()){
            OVertex vertex1 = db.load(a); // 从数据库中加载一个指定类型的记录并返回其对象
            OVertex vertex2 = db.load(b);
            vertex1.addEdge(vertex2,edge).save();//保存
        }finally {
            close();
        }

    }

//    public static <T>T getQuery(){
//
//        return null;
//    }

    /**
     *
     * @param databaseName
     * @return
     */
//    public static Map<String, String> getAllVertex(String databaseName){
//
//    }


    /**
     * 通过类名查询图中节点的ID
     * @param databaseName 数据库名
     * @param clazz 需要查询的顶点类名
     * @return 返回一个包含节点类名和RID的map
     */
    public static List<Map<String, String>> getAllVertexIDByClass(String databaseName, String clazz){
        connect(databaseName);
        //定义游标，每次获取1000条记录，避免内存溢出
        int batchSize = 1000;
        Map<String, String> map = new HashMap<>();
        List<Map<String, String>> vertexMap = new ArrayList<>();
        try(ODatabaseSession db = pool.acquire()){
            String statement = "SELECT FROM ?";
            OResultSet rs = db.query(statement, clazz, batchSize);
            while(rs.hasNext()){
                OVertex vertex = rs.next().getVertex().get();
                map.put(vertex.getSchemaType().get().toString(), String.valueOf(vertex.getIdentity()));
                vertexMap.add(map);
            }
            rs.close();
        }finally {
            close();
        }
        return vertexMap;
    }

    /**
     * 通过传入类名，获取所有该类型的节点的信息（包括RID与所有属性名）
     * @param databaseName
     * @param clazz
     * @return
     */
    public static List<Map<Map<String, String>, Set<String>>> getAllVertexInfoByClass(String databaseName, String clazz){
        //定义游标，每次获取1000条记录，避免内存溢出
        connect(databaseName);
        int batchSize = 1000;
        Map<String, String> mapClassRid; //保存节点类名和RID信息的键值对
        Map<Map<String, String>, Set<String>> vertexAllInfo; //Set保存了节点的所有属性，将类名和RID组成的键值对与对应节点的所有属性组成一个键值对
        List<Map<Map<String, String>, Set<String>>> vertexList = new ArrayList<>(); //用列表保存所有节点信息
        try(ODatabaseSession db = pool.acquire()){
            String statement = "SELECT FROM ?";
            OResultSet rs = db.query(statement, clazz, batchSize);
            while(rs.hasNext()){
                //在循环外new了一个对象,表示这个对象的地址已经在内存中开辟出来了,保存在固定的位置.
                //这个时候我们做add()操作,只能增加list的个数,但是对象的地址只保存了一条数据,所以list里面的数据就会被最后一条全部覆盖了
                vertexAllInfo = new HashMap<>(); //需要在循环内new对象，否则put或者add时会覆盖前面的值
                mapClassRid = new HashMap<>();
                OVertex vertex = rs.next().getVertex().get();
                Set<String> properties = vertex.getPropertyNames(); //获取属性
                mapClassRid.put(vertex.getSchemaType().get().toString(), String.valueOf(vertex.getIdentity()));
                vertexAllInfo.put(mapClassRid, properties); //保存类名信息和属性信息
                vertexList.add(vertexAllInfo);
            }
            rs.close();
        }finally {
            close();
        }
        return vertexList;
    }

    /**
     * 两个顶点之间的所有路径的遍历
     */
    /**********************************************************************/

    /**
     * 深度优先遍历两个顶点之间的所有路径
     * @param db
     * @param currentVertexRID 起始顶点
     * @param targetVertexRID  目标顶点
     * @param path             每条路径
     * @param allPaths         所有路径
     */
    public static void traverseDFS(ODatabaseDocument db, String currentVertexRID, String targetVertexRID, List<String> path, List<List<String>> allPaths) {
        if (currentVertexRID.equals(targetVertexRID)) {
            // 当前顶点为目标顶点，将路径保存到容器中
            allPaths.add(new ArrayList<>(path));
        } else {
            int batchSize = 1000;
            // 查询当前顶点的出边
            String sql = "SELECT expand(out()) FROM " + currentVertexRID;
            OResultSet resultSet = db.query(sql, batchSize);
            if(resultSet != null){
                while (resultSet.hasNext()) {
                    OResult result = resultSet.next();
                    String nextVertexRID = result.getIdentity().get().toString();
                    if (!path.contains(nextVertexRID)) {
                        // 避免环路，将下一个顶点加入路径中
                        path.add(nextVertexRID);
                        // 递归遍历下一个顶点
                        traverseDFS(db, nextVertexRID, targetVertexRID, path, allPaths);
                        // 从路径中移除当前顶点，以便探索其他路径
                        path.remove(nextVertexRID);
                    }
                }
                resultSet.close();
            }

        }
    }

    /**
     * 调用上面的traverseDFS()函数进行遍历
     * @param databaseName
     * @param fromVertexRID
     * @param toVertexRID
     * @return
     */
    public static List<List<String>> getAllPathFromVToVTest(String databaseName, String fromVertexRID, String toVertexRID){
        connect(databaseName);
        try(ODatabaseSession db = pool.acquire()){
            if(fromVertexRID.equals(toVertexRID)){
                System.out.println("无到达路径");
            }
            List<List<String>> allPaths = new ArrayList<>();
            // 执行深度优先遍历
            traverseDFS(db, fromVertexRID, toVertexRID, new ArrayList<>(), allPaths);
            if(!allPaths.isEmpty()){
                // 输出所有路径
                System.out.println("从" + fromVertexRID + "到" + toVertexRID + "的所有路径:");
                for (List<String> path : allPaths) {
                    System.out.println(path);
                }
            }
            return allPaths;
        }finally {
            close();
        }
    }

    /**
     * 最短路径的遍历
     */
    /***********************************************************************************************/

    /**
     * 深度优先遍历最短路径
     * @param db
     * @param currentVertexRID
     * @param targetVertexRID
     * @param currentPath 保存遍历的每一条路径，再与shortestPath对比
     * @param shortestPath 保存最短路径
     * @param minPathLen 保存最短路径长度
     */
    public static void traverseShortestDFS(ODatabaseDocument db, String currentVertexRID, String targetVertexRID, List<String> currentPath, List<String> shortestPath, int[] minPathLen){
        if (currentVertexRID.equals(targetVertexRID)) {
            // 当前顶点为目标顶点，将路径保存到容器中
            if(minPathLen[0] > currentPath.size()){
                minPathLen[0] = currentPath.size();
                shortestPath.clear();
                shortestPath.addAll(currentPath);
            }
        } else {
            int batchSize = 1000;
            // 查询当前顶点的出边
            String sql = "SELECT expand(out()) FROM " + currentVertexRID;
            OResultSet resultSet = db.query(sql, batchSize);
            if(resultSet != null){
                while (resultSet.hasNext()) {
                    OResult result = resultSet.next();
                    String nextVertexRID = result.getIdentity().get().toString();
                    if (!currentPath.contains(nextVertexRID)) {
                        // 避免环路，将下一个顶点加入路径中
                        currentPath.add(nextVertexRID);
                        // 递归遍历下一个顶点
                        traverseShortestDFS(db, nextVertexRID, targetVertexRID, currentPath, shortestPath, minPathLen);
                        // 从路径中移除当前顶点，以便探索其他路径
                        currentPath.remove(nextVertexRID);
                    }
                }
                resultSet.close();
            }
        }
    }


    /**
     * 调用上面的函数进行最短路径遍查找
     * @param databaseName
     * @param fromVertexRID
     * @param toVertexRID
     * @return
     */
    public static List<String> getShortestPathFromVToV(String databaseName, String fromVertexRID, String toVertexRID){
        connect(databaseName);
        List<String> shortestPath = new ArrayList<>(); //保存最短路径
        int[] minPathLen = new int[1]; //保存最短路径长度
        minPathLen[0] = Integer.MAX_VALUE;
        try(ODatabaseSession db = pool.acquire()){
            if(fromVertexRID.equals(toVertexRID)){
                System.out.println("无到达路径");
            }
            // 执行深度优先遍历
            traverseShortestDFS(db, fromVertexRID, toVertexRID, new ArrayList<>(), shortestPath, minPathLen);
            if(!shortestPath.isEmpty()){
                // 输出最短路径
                System.out.println("从" + fromVertexRID + "到" + toVertexRID + "的最短路径:");
                System.out.println(shortestPath);
            }
            return shortestPath;
        }finally {
            close();
        }
    }


    /**
     * 获取所有节点的信息（类名，RID，节点属性），并按照不同节点类名保存在Map
     * @param databaseName
     * @return
     */
    public static Map<String, Map<String, OElement>> getAllVertexInfo(String databaseName){
        connect(databaseName);
        Map<String, Map<String, OElement>> classifiedNodes = new HashMap<>();
        try(ODatabaseSession db = pool.acquire()){
            String sql = "SELECT @class, * FROM V"; // V 表示图中的顶点类
            OResultSet resultSet = db.query(sql);

            while (resultSet.hasNext()) {
                OElement element = resultSet.next().toElement();
                String className = element.getSchemaType().get().toString();
                String nodeId = element.getIdentity().toString();

                System.out.println("节点RID: " + nodeId);
                System.out.println("节点类型: " + className);
                System.out.println("节点属性: " + element.toJSON());
                System.out.println("------------------------------------------------");

                if (!classifiedNodes.containsKey(className)) {
                    classifiedNodes.put(className, new HashMap<>());
                }

                Map<String, OElement> nodesOfType = classifiedNodes.get(className);
                nodesOfType.put(nodeId, element);
            }
        }finally {
            close();
        }
        return classifiedNodes;
    }
}
