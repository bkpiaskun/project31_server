package com.storageapp.model;

import java.sql.*;
import java.util.*;
    /**
     * Class used to connect to MariaDB database
     */
public class MariaDB {

    private static  String JDBC_DRIVER;
    private static  String DB_URL;
    private static  String USER;
    private static  String PASS;

    /**
     * Configures the connection to the database
     */
    public static void Configure() {
        System.out.println("Czy chcesz wczytać domyślną konfiguracje ?[T/N] ");
        Scanner verd = new Scanner(System.in);
        String c = verd.nextLine();
        if(c.equals("t") || c.equals("T"))
        {
            JDBC_DRIVER = "org.mariadb.jdbc.Driver";
            DB_URL = "jdbc:mariadb://178.159.136.124";
            USER = "root";
            PASS = "root";
            System.out.println("Wczytano domyślną konfiguracje.");
        } else {
            if(c.equals("n") || c.equals("N"))
            {
                JDBC_DRIVER = "org.mariadb.jdbc.Driver";
                System.out.println("Podaj IP bazy");
                Scanner verd2 = new Scanner(System.in);
                String cc = verd2.nextLine();
                DB_URL = "jdbc:mariadb://"+cc.toString();
                System.out.println("Podaj Użytkownika");
                cc = verd2.nextLine();
                USER = cc.toString();
                System.out.println("Podaj Użytkownika");
                cc = verd2.nextLine();
                PASS = cc.toString();
                System.out.println("Wczytano konfiguracje do pliku.");
            }
        }

    }
    /**
     * Check if provided credentials matches with any of the database users
     *
     * @param  login  Login to the application
     * @param  HashedPassword Password to the application
     * @return      Boolean if there is such a user
     */
     public static Boolean IsAUser(String login, String HashedPassword) {
        Connection conn = null;
         PreparedStatement statement = null;
        Boolean isAUser = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String myStatement = "SELECT * FROM nisd.users where Login =(?) and PassHash = (?);";
            statement= conn.prepareStatement   (myStatement );
            statement.setString(1,login);
            statement.setString(2,HashedPassword);
            ResultSet rs = statement.executeQuery();
            statement.close();
            int count = 0;
            while (rs.next()) {
                ++count;
            }
            if (count == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return isAUser;
    }
    /**
         * Check if there is a user with such login or not.
         * @param Login to be checked
         * @return Boolean if there is already a user with this login
         */
    public static Boolean CheckLogin(String Login) {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement statement= conn.prepareStatement   ("SELECT * FROM nisd.users where Login =(?);" );
            statement.setString(1,Login);
            ResultSet rs = statement.executeQuery();
            statement.close();
            int count = 0;
            while (rs.next()) {
                ++count;
            }
            if(count == 1)
            {
                return true;
            }
            else
            {
                return false;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Register user with provided credentials
     *
     * @param  login  Login to the application
     * @param  HashedPassword Password to the application
     * @return      Boolean if the method has succeed
     */
    public static Boolean RegisterUser(String login, String HashedPassword) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);


            String query;

            if (CheckLogin(login)) {
                return false;
            } else {
                query = "INSERT INTO nisd.users (Login, PassHash) VALUES ((?), (?));";




                PreparedStatement statement= conn.prepareStatement   (query );
                statement.setString(1,login);
                statement.setString(2,HashedPassword);
                statement.executeQuery();
                statement.close();
                return true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return false;
    }
    /**
     *Add's password connected to such Login and UserPass
     *
     * @param  Login  Login to the application
     * @param  UserPass Password to the application
     * @param  HashedPass Password to domain we wanted to store
     * @param  Dest Domain we want to store passwords for
     * @param  DestLogin Login to domain we wanted to store passwords
     * @return      Boolean if the method has succeed
     */
    public static Boolean AddPassword(String Login, String UserPass, String HashedPass, String Dest, String DestLogin) {
        if (IsAUser(Login, UserPass)) {
            Connection conn = null;
            Statement stmt = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(
                        DB_URL, USER, PASS);
                stmt = conn.createStatement();
                String query = "INSERT INTO nisd.passwords (Owner_ID,Hashed_Password,Destination,Destination_User) VALUES ((select nisd.users.User_ID from nisd.users where nisd.users.Login = (?) and nisd.users.PassHash = (?)),(?), (?),(?));";



                PreparedStatement statement= conn.prepareStatement   (query );
                statement.setString(1,Login);
                statement.setString(2,UserPass);
                statement.setString(3,HashedPass);
                statement.setString(4,Dest);
                statement.setString(5,DestLogin);

                statement.executeQuery();
                statement.close();
                return true;
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return false;
    }
    /**
     *List password's connected to such Login and UserPass
     *
     * @param  Login  Login to the application
     * @param  UserPass Password to the application
     * @return      List containing Map of the passwords
     */
    public static List<    Map< String, Object >    > ListPasswords(String Login, String UserPass) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> row;
        Connection conn = null;
        PreparedStatement statement = null;
        if(IsAUser(Login,UserPass))
        {
            try {

                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);





                String query = "select * FROM nisd.passwords INNER JOIN nisd.users ON nisd.passwords.Owner_ID = nisd.users.User_ID where nisd.users.Login = (?) and nisd.users.PassHash = (?);";


                statement= conn.prepareStatement   (query );
                statement.setString(1,Login);
                statement.setString(2,UserPass);


                ResultSet rs = statement.executeQuery();
                statement.close();

                ResultSetMetaData metaData = rs.getMetaData();
                Integer columnCount = metaData.getColumnCount();
                while (rs.next()) {
                    row = new HashMap<String, Object>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
    return resultList;
    }
    /**
     *Remove's password connected to such Login and UserPass with such ID
     *
     * @param  Login  Login to the application
     * @param  UserPass Password to the application
     * @param  ID ID of the password in database
     * @return      Boolean if the method has succeed
     */
    public static Boolean RemovePassword(String Login, String UserPass, int ID) {
        if (IsAUser(Login, UserPass)) {
            Connection conn = null;
            PreparedStatement statement = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(
                        DB_URL, USER, PASS);

                String query = "DELETE nisd.passwords FROM nisd.passwords INNER JOIN nisd.users ON nisd.passwords.Owner_ID = nisd.users.User_ID where nisd.passwords.Pass_ID = (?) and nisd.users.Login = (?) and nisd.users.PassHash = (?);";

                statement= conn.prepareStatement   (query );
                statement.setInt(1,ID);
                statement.setString(2,Login);
                statement.setString(3,UserPass);
                statement.executeQuery();
                statement.close();

                return true;
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if ( statement != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return false;
    }
    /**
     *Modifies password connected to such Login and UserPass with such ID
     *
     * @param  Login  Login to the application
     * @param  UserPass Password to the application
     * @param  ID ID of the password in database
     * @param  HashedPass Password to domain we wanted to store
     * @param  Dest Domain we want to store passwords for
     * @param  DestLogin Login to domain we wanted to store passwords
     * @return      Boolean if the method has succeed
     */
    public static Boolean ModifyPassword(String Login, String UserPass, String HashedPass, String Dest, String DestLogin, int ID) {
        if (IsAUser(Login, UserPass)) {
            Connection conn = null;
            PreparedStatement statement = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(
                        DB_URL, USER, PASS);

                String query = "update nisd.passwords " +
                        "set " + "Destination = (?)," +
                        "Destination_User = (?)," +
                        "Hashed_Password = (?)," +
                        "Owner_ID = (select nisd.users.User_ID from nisd.users where nisd.users.Login = (?) and nisd.users.PassHash = (?))" +
                        "where Pass_ID = '"+ID+"';";
                statement = conn.prepareStatement(query);

                statement.setString(1,Dest);
                statement.setString(2,DestLogin);
                statement.setString(3,HashedPass);
                statement.setString(4,Login);
                statement.setString(5,UserPass);
                statement.setInt(6,ID);


                statement.executeQuery(query);
                statement.close();
                return true;
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return false;
    }
    /**
     *Drops all the passwords connected to user with such Login and Password.
     *
     * @param  Login  Login to the application
     * @param  UserPass Password to the application
     * @return      Boolean if the method has succeed
     */
    public static Boolean DropPasswords(String Login, String UserPass) {
        if (IsAUser(Login, UserPass)) {
            Connection conn = null;
            PreparedStatement statement = null;
            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(
                        DB_URL, USER, PASS);

                String query = "DELETE nisd.passwords FROM nisd.passwords INNER JOIN nisd.users ON nisd.passwords.Owner_ID = nisd.users.User_ID where nisd.users.Login = (?) and nisd.users.PassHash =(?);";


                statement= conn.prepareStatement   (query );
                statement.setString(1,Login);
                statement.setString(2,UserPass);

                statement.executeQuery();
                statement.close();



                return true;
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return false;
    }
}