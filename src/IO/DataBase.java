package IO;

import Support.DataNameUtils;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.*;

public class DataBase implements DataBaseIF {

    public static final String URL = "jdbc:mysql://localhost:3306/carrentsystem?serverTimezone=UTC&useSSL=false";
    public static final String USER = "root";
    public static final String PASSWORD = "root";

    private Connection connection;

    /**
     * singleton
     */
    private static class InnerHelper {
        private final static DataBase dataBase = new DataBase();
    }

    private DataBase() {
    }

    public static DataBase getInstance() {
        return InnerHelper.dataBase;
    }

    /**
     * @return status of connection
     */

    public boolean initConnect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 创建数据
     */
    public void initData() throws SQLException {
        Statement statement = connection.createStatement();
        //创建customer
        Random random = new Random();
        int c_id = 2100,s_id = 2000,info = 0;

        String[] license = new String[100];
        String sql1, sql2,sql3,sql4,sql5,sql6,sql7;
        String[] first = new String[]{"范","黄","叶","蔡","赵","钱","孙","李","周","揭"};
        String[] second = new String[]{"小","鹏","人","风","水","好","笑","天","佳","平","爱","未","年","时","苗"};
        String[] area = new String[]{"鄂","京","粤","赣","皖","琼","黑","辽","晋","苏"};
        String[] brand = new String[]{"奔驰","宝马","特斯拉","五菱宏光","大众","宾利","路虎","本田","一汽","现代"};
        String[] cnames = new String[100];
        String[] snames = new String[100];
        for(int i = 0; i < 100; i++){
            cnames[i] = first[random.nextInt(10)] + second[random.nextInt(15)] + second[random.nextInt(15)];
            snames[i] = first[random.nextInt(10)] + second[random.nextInt(15)] + second[random.nextInt(15)];
            license[i] = area[random.nextInt(10)] + (char)('A' + random.nextInt(26)) + (1000 + random.nextInt(9000));
        }
        for(int i = 0; i < 100; i++){
            int s_time = (10000*(2017 + random.nextInt(5))+100*random.nextInt(13)+random.nextInt(29));
            int e_time = s_time + 2;
            sql1 = String.format("INSERT INTO customer VALUES (%d,\"%s\", %d ,'N')",c_id,cnames[i],random.nextInt(50));
            sql2 = String.format("INSERT INTO stuff VALUES (%d,\"%s\", %d)",s_id,snames[i],random.nextInt(50));
            sql3 = String.format("INSERT INTO car VALUES (\"%s\",\"%s\",%d, %d,%d)",license[i],brand[random.nextInt(10)],random.nextInt(100),random.nextInt(6),100+random.nextInt(200));
            sql4 = String.format("INSERT INTO info VALUES (%d,\"%s\",%d,%d,%d,%d,NULL,%d)",info++,license[i],c_id,s_id,3,0,s_time);
            sql5 = String.format("INSERT INTO info VALUES (%d,\"%s\",%d,%d,%d,%d,NULL,%d)",info++,license[i],c_id,s_id,4,0,e_time);
            sql6 = String.format("INSERT INTO info VALUES (%d,\"%s\",%d,%d,%d,%d,NULL,%d)",info++,license[i],c_id,s_id,1,0,e_time - 1);
            sql7 = String.format("INSERT INTO info VALUES (%d,\"%s\",%d,%d,%d,%d,NULL,%d)",info++,license[i],c_id++,s_id++,2,0,e_time - 1);
            System.out.println(sql1 + "\n" + sql2 + "\n" + sql3+ "\n" + sql4+ "\n" + sql5+ "\n" );
            statement.execute(sql1);
            statement.execute(sql2);
            statement.execute(sql3);
            statement.execute(sql4);
            statement.execute(sql5);
            if(random.nextInt(2) == 1){
                System.out.println(sql6);
                statement.execute(sql6);

            }
            if(random.nextInt(2) == 1){
                statement.execute(sql7);
                System.out.println(sql7);
            }

            System.out.println(c_id + s_id);
        }


    }

    /**
     * return users' authority
     *
     * @param name 输入的用户名
     * @param psw  输入的密码
     * @return authority, -1->no found, 1->root user, 2->administrator, 3->normal user
     * @throws SQLException
     */

    @Override
    public int checkUser(String name, String psw, int author) throws SQLException {
//        initData();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");//全表搜索
        while (resultSet.next()) {
            if (resultSet.getString("name").equals(name)) {
                if (resultSet.getString("password").equals(psw))
                if(resultSet.getInt("author") == author)
                {
                    return resultSet.getInt("customerid");
                }
            }
        }
        statement.close();
        return -1;
    }

    public void updatePsw(String name,String psw) throws SQLException {
        Statement statement = connection.createStatement();
        String sql1 = String.format("UPDATE users SET password = %s WHERE name = \"%s\"",psw ,name);
        boolean resultSet = statement.execute(sql1);

        System.out.println(sql1);
    }

    /**
     * 注册用户
     * @param name
     * @param psw
     * @param author
     * @return result
     * @throws SQLException
     */
    public int registerUser(String name, String psw, int author,String id) throws SQLException {
        Statement statement = connection.createStatement();
        String sql1 = String.format("SELECT * FROM users WHERE name = \"%s\" ",name);
        ResultSet resultSet = statement.executeQuery(sql1);
        if(resultSet.next()){
            return -1;
        }
        System.out.println(sql1);
        String sql2;
        if(author == 0)
        sql2 = String.format("INSERT INTO users VALUES (\"%s\", \"%s\", %d,%d,null)",  name, psw,author,Integer.valueOf(id));
        else sql2 = String.format("INSERT INTO users VALUES (\"%s\", \"%s\", %d,null,null)",  name, psw,author);

        System.out.println(sql2);
        statement.execute(sql2);
        //boolean resultSet = statement.execute("INSERT INTO users VALUE name, psw,author,0");//全表搜索

        statement.close();
        return 0;
    }

    public ResultSet getUserDetail(int customerid) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = String.format("SELECT * FROM customer WHERE id = %d ",customerid);
        ResultSet resultSet = statement.executeQuery(sql);
        System.out.println(sql);
        return resultSet;
    }

    public ResultSet getStuffDetail(int customerid) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = String.format("SELECT * FROM customer WHERE id = %d ",customerid);
        ResultSet resultSet = statement.executeQuery(sql);
        System.out.println(sql);
        return resultSet;
    }

    //从约束里拿where子句
    private String getWhereClause(HashMap<String, String> map, boolean haveWhere) {
        StringBuilder sql = new StringBuilder();
        boolean isFirst = true;
        if (haveWhere) {
            isFirst = false;
        }
        Set<String> keySet = map.keySet();

        for (String key : keySet) {
            String value = map.get(key);
            if (value.equals("")) continue;
            if (isFirst) {
                sql.append("WHERE ");
                isFirst = false;
            } else {
                sql.append("and ");
            }

            if (DataNameUtils.name2name(key).equals("event"))
                value = String.valueOf(DataNameUtils.eventName.indexOf(value) + 1);
            sql.append(DataNameUtils.name2name(key) + " = '" + value + "' ");
        }
        return String.valueOf(sql);
    }

    //压一个二维list出来
    private Vector<Vector<String>> pullVectors(ResultSet resultSet, int length) throws SQLException {
        Vector<Vector<String>> vectors = new Vector<>();
        while (resultSet.next()) {
            Vector<String> tempVec = new Vector<>();
            for (int i = 1; i <= length; i++) {//从1开始
                tempVec.add(resultSet.getString(i));
            }
            vectors.add(tempVec);
        }
        return vectors;
    }

    /**
     * getLists为从数据库中获得对应的数据，只能支持普通查询（车辆，顾客，）
     * three override methods for each one
     *
     * @return 获得的数据项
     * @throws SQLException 语句错误或者受到约束
     */
    @Override
    public Vector<Vector<String>> getCarLists() throws SQLException {
        String sql = "SELECT * FROM car ";
        return getCarLists(sql);
    }

    @Override
    public Vector<Vector<String>> getCarLists(HashMap<String, String> map) throws SQLException {
        String sql = "SELECT * FROM car ";
        sql = sql + getWhereClause(map, false);
        return getCarLists(sql);
    }

    @Override
    public Vector<Vector<String>> getCarLists(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        Vector<Vector<String>> vectors = pullVectors(resultSet, DataNameUtils.carColumns.length);
        statement.close();
        return vectors;
    }

    @Override
    public Vector<Vector<String>> getCustomerLists() throws SQLException {
        String sql = "SELECT * FROM customer ";
        return getCustomerLists(sql);
    }

    @Override
    public Vector<Vector<String>> getCustomerLists(HashMap<String, String> map) throws SQLException {
        String sql = "SELECT * FROM customer ";
        sql = sql + getWhereClause(map, false);
        return getCustomerLists(sql);
    }

    @Override
    public Vector<Vector<String>> getCustomerLists(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        Vector<Vector<String>> vectors = pullVectors(resultSet, DataNameUtils.customerColumns.length);
        statement.close();
        return vectors;
    }

    @Override
    public Vector<Vector<String>> getStuffLists() throws SQLException {
        String sql = "SELECT * FROM stuff ";
        return getStuffLists(sql);
    }

    @Override
    public Vector<Vector<String>> getStuffLists(HashMap<String, String> map) throws SQLException {
        String sql = "SELECT * FROM stuff ";
        sql = sql + getWhereClause(map, false);
        return getStuffLists(sql);
    }

    @Override
    public Vector<Vector<String>> getStuffLists(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        Vector<Vector<String>> vectors = pullVectors(resultSet, DataNameUtils.stuffColumns.length);
        statement.close();
        return vectors;
    }


    //权限3用户只能查看自己的用户信息，和修改用户名和密码
    @Override
    public Vector<Vector<String>> getUserLists(int authority, String userName) throws SQLException {
        String sql = null;
        if (authority == 2) {
            sql = "SELECT * FROM users ";
        } else if (authority == 1) {
            sql = "SELECT * FROM users WHERE author <> 2 ";
        } else if (authority == 0) {
            sql = "SELECT * FROM users WHERE name = '" + userName + "' ";
        }
        return getUserLists(sql);
    }

    @Override
    public Vector<Vector<String>> getUserLists(HashMap<String, String> map, int authority, String userName) throws SQLException {
        String sql = null;
        if (authority == 1) {
            sql = "SELECT * FROM users ";
            sql = sql + getWhereClause(map, false);
        } else if (authority == 2) {
            sql = "SELECT * FROM users WHERE author <> 1 ";
        } else if (authority == 3) {
            sql = "SELECT * FROM users WHERE name = '" + userName + "' ";
            sql = sql + getWhereClause(map, true);
        }
        return getUserLists(sql);
    }

    @Override
    public Vector<Vector<String>> getUserLists(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(sql);
        ResultSet resultSet = statement.executeQuery(sql);
        Vector<Vector<String>> vectors = pullVectors(resultSet, DataNameUtils.usersColumns.length);
        statement.close();
        return vectors;
    }

    @Override
    public Vector<Vector<String>> getInfoLists(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(sql);
        ResultSet resultSet = statement.executeQuery(sql);

        Vector<Vector<String>> vectors = new Vector<>();
        while (resultSet.next()) {
            Vector<String> tempVec = new Vector<>();
            for (int i = 1; i <= DataNameUtils.infoColumns.length; i++) {//从1开始
                if (i == 6) {
                    switch (resultSet.getInt(i)) {//eventid to event
                        case 1:
                            tempVec.add("损坏维修");
                            break;
                        case 2:
                            tempVec.add("罚款");
                            break;
                        case 3:
                            tempVec.add("借车");
                            break;
                        case 4:
                            tempVec.add("还车");
                            break;
                    }
                } else {
                    tempVec.add(resultSet.getString(i));
                }
            }
            vectors.add(tempVec);
        }

        statement.close();
        return vectors;
    }

    @Override
    public Vector<Vector<String>> getInfoLists(HashMap<String, String> map, int authority, String userName) throws SQLException {
        String sql = "SELECT info.infoid , info.moychange, car.license, customer.id,customer.name, info.event, info.detailevent, info.time, stuff.id,stuff.name\n" +
                "FROM info,car,stuff,customer\n" +
                "WHERE info.license = car.license AND info.stuffid = stuff.id AND customer.id = info.customerid ";
        sql = sql + getWhereClause(map, true);
        return getInfoLists(sql);
    }

    @Override
    public Vector<Vector<String>> getInfoLists(int authority, String userName) throws SQLException {
        String sql = "SELECT info.infoid , info.moychange, car.license, customer.id,customer.name, info.event, info.detailevent, info.time, stuff.id,stuff.name\n" +
                "FROM info,car,stuff,customer\n" +
                "WHERE info.license = car.license AND info.stuffid = stuff.id AND customer.id = info.customerid ";
        if (authority == 0) {
            sql = sql + "AND customer.id = " + getIDbyUserName(userName) + " ";
        }
        return getInfoLists(sql);
    }

    @Override
    public String getIDbyUserName(String name) throws SQLException {
        String sql = String.format("SELECT customerid FROM users WHERE NAME = '%s'", name);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        Vector<String> vector = new Vector<>();
        while (resultSet.next()) {
            vector.add(resultSet.getString(1));
        }
        statement.close();
        return vector.get(0);
    }

    /**
     * 在数据库中级联删除一行
     *
     * @param tableMode  表格模式的标志字符串
     * @param primaryKey 主键的值
     * @throws SQLException 语句错误或者受到约束
     */

    @Override
    public void deleteRow(String tableMode, String primaryKey) throws SQLException {
        String tableName = DataNameUtils.tableMode2Name(tableMode);

        String primaryKeyName = DataNameUtils.primaryKeyMap.get(tableName);
        if (primaryKeyName == null) primaryKeyName = "id";
        String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, primaryKeyName);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, primaryKey);
        System.out.println(preparedStatement.toString());
        preparedStatement.execute();

    }

    /**
     * 更新数据
     *
     * @param tableMode  表格模式的标志字符串
     * @param name       更新属性的列名
     * @param value      更新后的值
     * @param primaryKey 主键的值
     * @throws SQLException 语句错误或者受到约束
     */


    @Override
    public void updateData(String tableMode, String name, String value, String primaryKey) throws SQLException {
        String tableName = DataNameUtils.tableMode2Name(tableMode);

        if (name.equals("事件")) value = String.valueOf(DataNameUtils.eventName.indexOf(value) + 1);

        //拿到主键的名字
        String primaryKeyName = DataNameUtils.primaryKeyMap.get(tableName);
        if (primaryKeyName == null) primaryKeyName = "id";

        String sql = String.format("UPDATE %s SET %s = ? where %s = ?", tableName, DataNameUtils.name2name(name), primaryKeyName);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        if (value.equals("")) {
            preparedStatement.setNull(1, Types.CHAR);//this types.* is useless...
        } else {
            preparedStatement.setString(1, value);
        }
        preparedStatement.setString(2, primaryKey);
        System.out.println(preparedStatement.toString());
        preparedStatement.execute();
    }

    /**
     * 添加行
     *
     * @param tableMode 表格模式的标志字符串
     * @param data      一张哈希表存新数据
     * @throws SQLException 语句错误或者受到约束
     */

    @Override
    public void addRow(String tableMode, HashMap<String, String> data) throws SQLException {
        Statement statement = connection.createStatement();
        String tableName = DataNameUtils.tableMode2Name(tableMode);
        if (tableName != null) {
            StringBuilder columns = new StringBuilder("");
            StringBuilder values = new StringBuilder("");
            Set<String> keySet = data.keySet();
            Iterator<String> iterator = keySet.iterator();

            boolean isFirst = true;
            while (iterator.hasNext()) {
                String s = iterator.next();
                System.out.println(s);
                String value = data.get(s);
                if (DataNameUtils.eventName.contains(value))
                    value = String.valueOf(DataNameUtils.eventName.indexOf(value) + 1);
                if (value != null && !value.equals("")) {
                    if (isFirst) {//第一个不加逗号
                        isFirst = false;
                        columns.append(DataNameUtils.name2name(s));
                        values.append("'" + value + "'");
                    } else {
                        columns.append("," + DataNameUtils.name2name(s));
                        values.append(",'" + value + "'");
                    }
                }
            }
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns.toString(), values.toString());
            System.out.println(sql);
            statement.execute(sql);
            statement.close();
        }
    }

    /**
     * 获得所有图表数据
     * 1->year 2->month 3->day
     * func:借车 还车 损坏维修 罚款 流水
     *
     * @return
     * @throws SQLException
     */
    @Override
    public ArrayList<ArrayList<String>> getAllChartData(String mode, String func) throws SQLException {

        String selectItem;
        String whereClause = "";
        if (func.equals("流水")) {
            selectItem = "SUM(moychange)";
        } else {
            selectItem = "COUNT(TIME)";//计算次数
            int indexid = DataNameUtils.eventName.indexOf(func) + 1;
            whereClause = " WHERE event = " + String.valueOf(indexid);
        }

        String sql = null;
        if (mode.equals("年")) {
            sql = String.format("SELECT YEAR(TIME), %s FROM info %s GROUP BY YEAR(TIME)", selectItem, whereClause);
        } else if (mode.equals("月")) {
            sql = String.format("SELECT YEAR(TIME),MONTH(TIME), %s FROM info %s GROUP BY YEAR(TIME),MONTH(TIME)", selectItem, whereClause);
        } else if (mode.equals("日")) {
            sql = String.format("SELECT TIME, %s FROM info %s GROUP BY TIME", selectItem, whereClause);
        }

        System.out.println(sql);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        while (resultSet.next()) {
            ArrayList<String> listTemp = new ArrayList<>();
            listTemp.add(resultSet.getString(1));
            listTemp.add(resultSet.getString(2));
            if (mode.equals("月")) listTemp.add(resultSet.getString(3));
            lists.add(listTemp);
        }
        statement.close();
        return lists;
    }

    public HashMap<String, Float> getCredit() throws SQLException {
        HashMap<String, Float> hashMap = new HashMap<>();

        for (int i = 1; i <= 4; i++) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getCreditClause(i));
            while (resultSet.next()) {
                String key = resultSet.getString(1);
                int times = resultSet.getInt(2);
                Float oldValue = hashMap.get(key);
                if (oldValue == null) oldValue = 0f;
                switch (i) {
                    case 1:
                        hashMap.put(key, oldValue - 3 * times);
                        break;
                    case 2:
                        hashMap.put(key, oldValue - 5 * times);
                        break;
                    case 3:
                        hashMap.put(key, oldValue + 2 * times);
                        break;
                    case 4:
                        hashMap.put(key, (float) (oldValue + 2.5 * times));
                        break;
                }
            }
            statement.close();
        }

        return hashMap;
    }

    private String getCreditClause(int eventid) {
        return "SELECT customerid,COUNT(customerid) \n" +
                "FROM info\n" +
                "WHERE EVENT = " + eventid + "\n" +
                "GROUP BY customerid";
    }

    public HashMap<String, String> getMapId2Name() throws SQLException {
        HashMap<String, String> hashMap = new HashMap<>();
        Statement statement = connection.createStatement();
        String sql = String.format("SELECT id, NAME\nFROM customer");
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            hashMap.put(resultSet.getString(1), resultSet.getString(2));

        }
        return hashMap;
    }

}
