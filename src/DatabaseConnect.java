
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author deepon
 */


public class DatabaseConnect {
    
    private  String dbURL = "jdbc:mysql://localhost:3306/schoolmanagementsystem";
    private  Connection connection = null;
    private  Statement stmt = null;
    private  String UserName_DB = "root";
    private  String Password_DB = "123456";
    private  ResultSet resultSet=null;
    private PreparedStatement pstmt=null;
    private String queryArray[];
    private String getRole = null;
    public static String identity = null;
    
    public DatabaseConnect()
    {
        queryArray = new String[10];
        queryArray[0] = "insert into user(UserID, Password, Role) values(?, ?, 'class_teacher')";
        queryArray[1] = "insert into personal_info(Name,Id,PresentAddress,PermanentAddress,ContactNo,Sex,Religion,Nationality,BloodGroup,DOB) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
        queryArray[2] = "insert into teacher (TeacherID , EmailID , ControllerID) values ( ? , ? , ? )";
        queryArray[3] = "insert into student (StudentID, FatherName, MotherName, Roll,ClassName, SectionName, GroupName, ControllerID) values (?, ?, ?, ?, ?, ?, ?, ? )";
        queryArray[4] = "insert into subject (SubjectID, SubjectName, ClassName, GroupName, ControllerID) values(?, ?, ?, ?, ?)";
        queryArray[5] = "update personal_info set Name=?,PresentAddress=?,PermanentAddress=?,ContactNo=?,Sex=?,Religion=?,Nationality=?,BloodGroup=?,DOB=? where Id=?";
        queryArray[6] = "update student set FatherName=?,MotherName=?,Roll=?,ClassName=?,SectionName=?,GroupName=?,ControllerID=? where StudentID=?";
        queryArray[7] = "update teacher set EmailID=?,ControllerID=? where TeacherID=?";
        
        try
        {
           //Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(dbURL, UserName_DB, Password_DB);
            //Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery(queryArray[0]);
         
        }
        
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void User(String id, String pwd)
    {
        try{
        pstmt = connection.prepareStatement(queryArray[0]);
        pstmt.setString(1, id);

        pstmt.setString(2, pwd);
        pstmt.executeUpdate();
        
        pstmt = connection.prepareStatement("insert into class_teacher(ControllerID) values(?)");
        pstmt.setString(1, id);
        pstmt.executeUpdate();
        }
        
        catch(Exception Ex)
        {
            Ex.printStackTrace();
        }
                 
    }
    
    public boolean getTeacherId(String id)
    {
        try
        {
            stmt  = connection.createStatement();
            
            resultSet = stmt.executeQuery("select TeacherID from teacher where TeacherID = '" + id +"'");
            
            if(resultSet.next() != false)
                return true;
        }
        
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return false;
    }
    
    public boolean isDataCorrect(String userID, String password)
    {
        try {
            
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("select UserID,Password,Role from user where UserID='"+userID+"' and Password='"+password+"'");
            
            if(resultSet.next() != false)
            {
                getRole = resultSet.getObject(3).toString();
               
                return true;
            }
            
            else
            {
                return false;
            }
        
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
             return false;
        }
       
    }
    
    public boolean getClassTeacher(String id)
    {
        try
        {
            stmt = connection.createStatement();
            
            resultSet = stmt.executeQuery("select * from class_teacher where ControllerID = '"+ id + "'");
            
            if(resultSet.next() != false)
                return true;
            
        }
        
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }
    
    public void deleteUser(String id)
    {
        try {
            pstmt = connection.prepareStatement("delete from `user` where `user`.`UserID` = ?");
            
            pstmt.setString(1, id);
            
            
            pstmt.executeUpdate();
            
            pstmt = connection.prepareStatement("delete from `class_teacher` where `class_teacher`.`ControllerID` = ?");
            
            pstmt.setString(1, id);
            
            
            pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
    
    public void passwordChange(String id, String pass)
    {
        try {
            pstmt = connection.prepareStatement("update user set Password = ? where UserID = ?");
            
            pstmt.setString(1, pass);
            pstmt.setString(2, id);
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public boolean logIn(String userID, String password)
    {
       if(isDataCorrect(userID, password) == true)
       {
           if(getRole.compareTo("administrator") == 0)
           {
               Administrator ad = new Administrator();
               ad.setVisible(true);
               
           }
           
           else
           {
               ClassTeacher con = new ClassTeacher();
               con.setVisible(true);
           }
           
           return true;
       }
       
       else
       {
           return false;
       }
    }
    
    public void insertPersonalInfo(String name,String id,String present,String permanent,String contact,String sex,String religion,String nation,String blood,String dob)
    {
        try{
        pstmt = connection.prepareStatement(queryArray[1]);
        pstmt.setString(1, name);

        pstmt.setString(2, id);
        pstmt.setString(3, present);
        pstmt.setString(4, permanent);
        pstmt.setString(5, contact);
        pstmt.setString(6, sex);
        pstmt.setString(7, religion);
        pstmt.setString(8, nation);
        pstmt.setString(9, blood);
        pstmt.setString(10, dob);
        
        pstmt.executeUpdate();
        }
        
        catch(Exception Ex)
        {
            Ex.printStackTrace();
        }
    }
    
    public void updatePersonalInfo(String name,String id,String present,String permanent,String contact,String sex,String religion,String nation,String blood,String dob)
    {
        try{
        pstmt = connection.prepareStatement(queryArray[5]);
        pstmt.setString(1, name);

        
        pstmt.setString(2, present);
        pstmt.setString(3, permanent);
        pstmt.setString(4, contact);
        pstmt.setString(5, sex);
        pstmt.setString(6, religion);
        pstmt.setString(7, nation);
        pstmt.setString(8, blood);
        pstmt.setString(9, dob);
        pstmt.setString(10, id);
        pstmt.executeUpdate();
        
        }
        
        catch(Exception Ex)
        {
            Ex.printStackTrace();
        }
    }
    
    public void insertTeacherInfo(String tchid , String email, String conid)
    {
        try{
        pstmt = connection.prepareStatement(queryArray[2]);
        pstmt.setString(1, tchid);

        pstmt.setString(2, email);
        pstmt.setString(3, conid);
        
        pstmt.executeUpdate();
        
        }
        catch(Exception Ex)
        {
            Ex.printStackTrace();
        }
    }
    
   public void updateTeacherInfo(String tchid , String email, String conid)
    {
        try{
        pstmt = connection.prepareStatement(queryArray[7]);
       

        pstmt.setString(1, email);
        pstmt.setString(2, conid);
         pstmt.setString(3, tchid);
        
        pstmt.executeUpdate();
        
        }
        catch(Exception Ex)
        {
            Ex.printStackTrace();
        }
    }
    
    public void insertStudentInfo(String stdid, String fatName, String motName, int roll, int className, String sectionName, String group_n, String conid)
    {
        try{
        pstmt = connection.prepareStatement(queryArray[3]);
        
        pstmt.setString(1, stdid);

        pstmt.setString(2, fatName);
        pstmt.setString(3, motName);
        pstmt.setInt(4, roll);
        pstmt.setInt(5, className);
        pstmt.setString(6, sectionName);
        pstmt.setString(7, group_n);
        pstmt.setString(8, conid);
        
      //  System.out.println(pstmt.toString());
        pstmt.executeUpdate();
        
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void updateStudentInfo(String stdid, String fatName, String motName, int roll, int className, String sectionName, String group_n, String conid)
    {
        try{
        pstmt = connection.prepareStatement(queryArray[6]);
        
        

        pstmt.setString(1, fatName);
        pstmt.setString(2, motName);
        pstmt.setInt(3, roll);
        pstmt.setInt(4, className);
        pstmt.setString(5, sectionName);
        pstmt.setString(6, group_n);
        pstmt.setString(7, conid);
        pstmt.setString(8, stdid);
      //  System.out.println(pstmt.toString());
        pstmt.executeUpdate();
        
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    
    public ResultSet getTeacherData(String tchId)
    {
        try {
            
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("select * from personal_info, teacher where personal_info.Id='"+tchId+"' and teacher.TeacherID='"+tchId+"'");
            
                 
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return resultSet;
    }
     public ResultSet getStudentData(String stdId)
    {
        try {
            
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("select * from personal_info, student where personal_info.Id='"+stdId+"' and student.StudentID='"+stdId+"'");   
                 
        }
        
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return resultSet;
    }
    
    public void insertSubject(String subId, String subName, int className, String grp)
    {
        try
        {
            pstmt = connection.prepareStatement(queryArray[4]);
            
            pstmt.setString(1, subId);
            pstmt.setString(2, subName);
            pstmt.setInt(3, className);
            pstmt.setString(4, grp);
            pstmt.setString(5, LoginWindow.conID);
            
            pstmt.executeUpdate();
            
           
        }
        
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public ResultSet loadSubject(String subId, int cls)
    {
        if(cls <= 5) cls = 5;
        
        else if(cls <= 8) cls = 8;
        
        else cls = 9;
        
        try {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("select SubjectName from subject where SubjectID = '"+subId+"' and ClassName = '"+cls+"'");
            
            
        } 
        
        catch (SQLException ex) {
            ex.printStackTrace();
        }
       return resultSet;
    }
    
    
    
    public void markEntryOneFive(JTable table, String term, String subId , String className)
    {
        String t,id;
        int i;
        
        if(term.equals("1")) t = "t1mark1_5";
        
        else if(term.equals("2")) t = "t2mark1_5";
        
        else t = "t3mark1_5";
        
        try
        {
            for(i = 0 ; i < 5 ; i++ )
            {  
                id = className + "_" + table.getModel().getValueAt(i, 0).toString();
                //"update teacher set EmailID=?,ControllerID=? where TeacherID=?";
                
          
                pstmt = connection.prepareStatement("UPDATE `schoolmanagementsystem`.`"+ t +"` SET `" + subId + "` = ? WHERE `"+ t +"`.`StudentID` = ?");
                
                pstmt.setString(1, table.getModel().getValueAt(i, 1).toString());
                pstmt.setString(2, id);
                System.out.println(pstmt.toString());
                pstmt.executeUpdate();
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    
     public void markEntrySixEight(JTable table, String term, String subId , String className)
    {
        String t,id;
        int i;
        
        if(term.equals("1")) t = "t1mark6_8";
        
        else if(term.equals("2")) t = "t2mark6_8";
        
        else t = "t3mark6_8";
        
        try
        {
            for(i = 0 ; i < 5 ; i++ )
            {  
                id = className + "_" + table.getModel().getValueAt(i, 0).toString();
                
                pstmt = connection.prepareStatement("UPDATE `schoolmanagementsystem`.`"+ t +"` SET `" + subId + "` = ? WHERE `"+ t +"`.`StudentID` = ?");
                
                pstmt.setString(1, table.getModel().getValueAt(i, 1).toString());
                pstmt.setString(2, id);
                System.out.println(pstmt.toString());
                pstmt.executeUpdate();
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
     public void markEntryNineTen(JTable table, String term, String subId , String className, String GroupName)
    {
        String t,id;
        int i;
        
        if(term.equals("1")) t = "t1mark9_10";
        
        else if(term.equals("2")) t = "t2mark9_10";
        
        else t = "t3mark9_10";
        
        try
        {
            for(i = 0 ; i < 5 ; i++ )
            {  
                id = className + "_" + table.getModel().getValueAt(i, 0).toString() + GroupName;
                
                pstmt = connection.prepareStatement("UPDATE `schoolmanagementsystem`.`"+ t +"` SET `" + subId + "` = ? WHERE `"+ t +"`.`StudentID` = ?");
                
                pstmt.setString(1, table.getModel().getValueAt(i, 1).toString());
                pstmt.setString(2, id);
                System.out.println(pstmt.toString());
                pstmt.executeUpdate();
            }
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
     
     
    public ResultSet loadSubjectMark(String subId, String term, String className, String id)
    {
            String t = null;
            int cls = Integer.parseInt(className);
         if(cls <= 5)
        {
            if(term.equals("1")) t = "t1mark1_5";
        
            else if(term.equals("2")) t = "t2mark1_5";
        
            else t = "t3mark1_5";
        }
        
        else if(cls <= 8)
        {
            if(term.equals("1")) t = "t1mark6_8";
        
            else if(term.equals("2")) t = "t2mark6_8";
        
            else t = "t3mark6_8";
        }
        
        else if(cls <= 10)
        {
            if(term.equals("1")) t = "t1mark9_10";
        
            else if(term.equals("2")) t = "t2mark9_10";
        
            else t = "t3mark9_10";
        }
            
            try
            {
                stmt = connection.createStatement();
                resultSet = stmt.executeQuery("select `"+ subId + "` from `"+ t + "` where `StudentID` = '" + id + "'");
            }

            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return resultSet;
    }
    
    
    public ResultSet loadIndividualMark(String id,String term, int cls)
    {   
        
        String t = null;
        
        if(cls <= 5)
        {
            if(term.equals("1")) t = "t1mark1_5";
        
            else if(term.equals("2")) t = "t2mark1_5";
        
            else t = "t3mark1_5";
        }
        
        else if(cls <= 8)
        {
            if(term.equals("1")) t = "t1mark6_8";
        
            else if(term.equals("2")) t = "t2mark6_8";
        
            else t = "t3mark6_8";
        }
        
        else if(cls <= 10)
        {
            if(term.equals("1")) t = "t1mark9_10";
        
            else if(term.equals("2")) t = "t2mark9_10";
        
            else t = "t3mark9_10";
        }
            
            try
            {
                stmt = connection.createStatement();
                resultSet = stmt.executeQuery("select * from "+ t + " where StudentID = '" + id +"'");
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            return resultSet;
    }
    
    public ResultSet getName(String id)
    {
        try
        {
            stmt = connection.createStatement();
            resultSet = stmt.executeQuery("select Name from personal_info where Id = '"+ id + "'");
        }
        
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        return resultSet;
    }
    
   /**************************************************
    * public void setStdId()
    {
        try {
            int i, j, t;
            String k, g, y = null;
            
            String a = "(`StudentID`, `101`, `102`, `107`, `108`, `109`, `127`, `145`, `134`, `129`, `111`) VALUES (?, '', '', '', '', '', '', '', '', '', '')";
            
            /*for(t = 1; t <= 3; t++)
            {
                if(t == 1) y = "Science";
                
                else if(t == 2) y = "Commerce";
                
                else y = "Arts";
            
            
            pstmt = connection.prepareStatement("insert into t1mark6_8 " + a);
            
            for(i = 6 ;i <= 8; i++)
            {
                g = String.valueOf(i);
                for(j = 1; j <= 10; j++)
                {
                    k = g + "_"+String.valueOf(j);
                    pstmt.setString(1, k);
                    pstmt.executeUpdate();
                }
                
            }
            
            pstmt = connection.prepareStatement("insert into t2mark6_8 " + a);
            
            for(i = 6; i <= 8; i++)
            {
                g = String.valueOf(i);
                for(j = 1; j <= 10; j++)
                {
                    k = g + "_"+String.valueOf(j);
                    pstmt.setString(1, k);
                    pstmt.executeUpdate();
                }
                
            }
            
            pstmt = connection.prepareStatement("insert into t3mark6_8 " + a);
          
            for(i = 6; i <= 8; i++)
            {
                g = String.valueOf(i);
                for(j = 1; j <= 10; j++)
                {
                    k = g +"_"+String.valueOf(j);
                    pstmt.setString(1, k);
                    pstmt.executeUpdate();
                }
               
            }
            
            //}
            
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    *******************************************/
   
    
}
