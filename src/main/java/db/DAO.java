package db;

import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAO extends DbConnector{
    public DAO() {
        super.connectToDB();
    }

    public User authorization(User user) throws SQLException {
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format("SELECT * FROM user WHERE login='%s';", user.getLogin()));
            if(rs.next()){
                if (user.getPassword().equals(rs.getString("password"))) {
                    user.setRole(rs.getString("role"));
                    user.setId(Integer.parseInt(rs.getString("user_id")));
                    return user;
                }
                else{
                    user.setRole("wrong");
                    return user;
                }
            }
            else {
                user.setRole("wrong");
                return user;
            }
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }


    //---------------------ПОЛУЧЕНИЕ ВСЕЙ ТАБЛИЦЫ-----------------------------------


    public ArrayList<Admin> getAllAdmins() throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format("SELECT * FROM user INNER JOIN person on user.person_id=person.person_id\n" +
                    "INNER JOIN admin on user.user_id=admin.user_id WHERE user.role='admin';"));
            ArrayList<Admin> adminList = new ArrayList<>();
            while(rs.next()){
                Admin admin = new Admin(
                        Integer.parseInt(rs.getString("user_id")),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("patronymic"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        Integer.parseInt(rs.getString("person_id")),
                        Integer.parseInt(rs.getString("admin_id")),
                        rs.getString("rights"),
                        rs.getString("block")
                );
                adminList.add(admin);
            }
            return adminList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Teacher> getAllTeachers() throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format("SELECT * FROM user INNER JOIN person on user.person_id=person.person_id\n" +
                    "INNER JOIN teacher on user.user_id=teacher.user_id WHERE user.role='teacher'\n"));
            ArrayList<Teacher> teacherList = new ArrayList<>();
            while(rs.next()){
                Teacher teacher = new Teacher(
                        Integer.parseInt(rs.getString("person_id")),
                        rs.getString("surname"),
                        rs.getString("name"),
                        rs.getString("patronymic"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        Integer.parseInt(rs.getString("user_id")),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role"),
                        Integer.parseInt(rs.getString("teacher_id")),
                        rs.getString("post"),
                        rs.getString("department")
                );
                teacherList.add(teacher);
            }
            return teacherList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Student> getAllStudents() throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM user " +
                            "INNER JOIN person ON user.person_id = person.person_id \n" +
                            "INNER JOIN student ON user.user_id = student.user_id \n" +
                            "INNER JOIN `group` ON student.group_id = `group`.group_id \n" +
                            "INNER JOIN speciality ON `group`.speciality_id = speciality.speciality_id \n" +
                            "INNER JOIN faculty ON speciality.faculty_id = faculty.faculty_id \n" +
                            "WHERE user.role = 'student'")
            );
            ArrayList<Student> studentList = new ArrayList<>();
            while(rs.next()){
//                String schedule[];
//                schedule = rs.getString("schedule").split("-", 14);
//                for(int i = 0; i < schedule.length; i++){
//                    if(schedule[i] == null) schedule[i] = "";
//                }
                Student student = new Student(
                        Integer.parseInt(rs.getString("person_id")),
                        rs.getString("surname"),
                        rs.getString("name"),
                        rs.getString("patronymic"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        Integer.parseInt(rs.getString("user_id")),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role"),
                        Integer.parseInt(rs.getString("student_id")),
                        rs.getString("DOB"),
                        Integer.parseInt(rs.getString("form_of_education")),
                        rs.getString("address"),
                        Integer.parseInt(rs.getString("group_id")),
                        Integer.parseInt(rs.getString("number_of_group")),
                        Integer.parseInt(rs.getString("faculty_id")),
                        rs.getString("name_of_faculty"),
                        Integer.parseInt(rs.getString("speciality_id")),
                        rs.getString("name_of_speciality")
                );
                studentList.add(student);
            }
            return studentList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Exam> getAllExams(Exam exam){
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM result " +
                    "INNER JOIN student ON result.student_id = student.student_id \n" +
                    "INNER JOIN subject_teacher ON result.subject_teacher_id = subject_teacher.subject_teacher_id \n" +
                    "INNER JOIN teacher ON subject_teacher.teacher_id = teacher.teacher_id \n" +
                    "INNER JOIN user on teacher.user_id=user.user_id\n" +
                    "INNER JOIN subject ON subject_teacher.subject_id = subject.subject_id \n" +
                    "INNER JOIN exam ON result.result_id = exam.result_id \n"+
                    "INNER JOIN person on user.person_id=person.person_id WHERE semester='%d';\n", exam.getSemester()
                    ));

            ArrayList<Exam> examsList = new ArrayList<>();
            while(rs.next()){
                Exam exam1 = new Exam(
                        Integer.parseInt(rs.getString("result_id")),
                        rs.getString("date"),
                        Integer.parseInt(rs.getString("semester")),
                        Integer.parseInt(rs.getString("student_id")),
                        Integer.parseInt(rs.getString("subject_teacher_id")),
                        rs.getString("name_of_subject"),
                        rs.getString("surname"),
                        Integer.parseInt(rs.getString("exam_id")),
                        Integer.parseInt(rs.getString("grade"))
                );
                examsList.add(exam1);
            }
            return examsList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Test> getAllTests(Test test){
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM result " +
                            "INNER JOIN student ON result.student_id = student.student_id \n" +
                            "INNER JOIN subject_teacher ON result.subject_teacher_id = subject_teacher.subject_teacher_id \n" +
                            "INNER JOIN teacher ON subject_teacher.teacher_id = teacher.teacher_id \n" +
                            "INNER JOIN user on teacher.user_id=user.user_id\n" +
                            "INNER JOIN subject ON subject_teacher.subject_id = subject.subject_id \n" +
                            "INNER JOIN test ON result.result_id = test.result_id \n"+
                            "INNER JOIN person on user.person_id=person.person_id WHERE semester='%d';\n", test.getSemester()
            ));

            ArrayList<Test> testsList = new ArrayList<>();
            while(rs.next()){
                Test test1 = new Test(
                        Integer.parseInt(rs.getString("result_id")),
                        rs.getString("date"),
                        Integer.parseInt(rs.getString("semester")),
                        Integer.parseInt(rs.getString("student_id")),
                        Integer.parseInt(rs.getString("subject_teacher_id")),
                        rs.getString("name_of_subject"),
                        rs.getString("surname"),
                        Integer.parseInt(rs.getString("test_id")),
                        rs.getString("is_passed")
                );
                testsList.add(test1);
            }
            return testsList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public Group getGroup(Group group) throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM `group` \n" +
                    "INNER JOIN speciality ON `group`.speciality_id = speciality.speciality_id \n" +
                    "INNER JOIN faculty ON speciality.faculty_id = faculty.faculty_id \n" +
                    "WHERE `group`.number_of_group = '%d';\n", group.getNumberOfGroup())
            );
//            ArrayList<Group> groupList = new ArrayList<>();
            Group group1 = new Group() ;
            while(rs.next()){
                group1 = new Group(
                        Integer.parseInt(rs.getString("faculty_id")),
                        rs.getString("name_of_faculty"),
                        Integer.parseInt(rs.getString("speciality_id")),
                        rs.getString("name_of_speciality"),
                        Integer.parseInt(rs.getString("group_id")),
                        Integer.parseInt(rs.getString("number_of_group"))
                );
            }
            return group1;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public Speciality getSpeciality(Speciality speciality) throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM speciality \n" +
                            "INNER JOIN faculty ON speciality.faculty_id = faculty.faculty_id \n" +
                            "WHERE speciality.name_of_speciality = '%s';\n", speciality.getSpecialityName())
            );
//            ArrayList<Group> groupList = new ArrayList<>();
            Speciality speciality1 = new Speciality() ;
            while(rs.next()){
                speciality1 = new Speciality(
                        Integer.parseInt(rs.getString("faculty_id")),
                        rs.getString("name_of_faculty"),
                        Integer.parseInt(rs.getString("speciality_id")),
                        rs.getString("name_of_speciality")
                );
            }
            return speciality1;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Group> getAllGroups() throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM `group` \n" +
                            "INNER JOIN speciality ON `group`.speciality_id = speciality.speciality_id \n" +
                            "INNER JOIN faculty ON speciality.faculty_id = faculty.faculty_id \n")
            );
            ArrayList<Group> groupsList = new ArrayList<>();
            while(rs.next()){
                Group group = new Group(
                        Integer.parseInt(rs.getString("faculty_id")),
                        rs.getString("name_of_faculty"),
                        Integer.parseInt(rs.getString("speciality_id")),
                        rs.getString("name_of_speciality"),
                        Integer.parseInt(rs.getString("group_id")),
                        Integer.parseInt(rs.getString("number_of_group"))
                );
                groupsList.add(group);
            }
            return groupsList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Speciality> getAllSpecialities() throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM speciality \n" +
                    "INNER JOIN faculty ON speciality.faculty_id = faculty.faculty_id \n")
            );
            ArrayList<Speciality> specialitiesList = new ArrayList<>();
            while(rs.next()){
                Speciality speciality = new Speciality(
                        Integer.parseInt(rs.getString("faculty_id")),
                        rs.getString("name_of_faculty"),
                        Integer.parseInt(rs.getString("speciality_id")),
                        rs.getString("name_of_speciality")
                );
                specialitiesList.add(speciality);
            }
            return specialitiesList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    public ArrayList<Subject> getAllSubjects() throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format("SELECT * FROM subject"));
            ArrayList<Subject> subjectsList = new ArrayList<>();
            while(rs.next()){
                Subject subject = new Subject(
                        Integer.parseInt(rs.getString("subject_id")),
                        rs.getString("name_of_subject")
                );
                subjectsList.add(subject);
            }
            return subjectsList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public ArrayList<Subject> getAllSubjectTeacher(Teacher teacher) throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format("SELECT * FROM subject_teacher\n" +
                    "INNER JOIN subject ON subject_teacher.subject_id = subject.subject_id \n" +
                    "INNER JOIN teacher ON subject_teacher.teacher_id = teacher.teacher_id \n" +
                    "WHERE teacher.teacher_id = '%d';\n", teacher.getId())
            );
            ArrayList<Subject> subjectsList = new ArrayList<>();
            while(rs.next()){
                Subject subject = new Subject(
                        Integer.parseInt(rs.getString("subject_id")),
                        rs.getString("name_of_subject")
                        );
                subjectsList.add(subject);
            }
            return subjectsList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public SubjectTeacher getSubjectTeacher(SubjectTeacher subjectTeacher) throws SQLException{
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format("SELECT * FROM subject_teacher\n" +
                    "WHERE subject_teacher.teacher_id = '%d' and subject_teacher.subject_id= '%d';\n", subjectTeacher.getId(), subjectTeacher.getSubjectId())
            );
            while(rs.next()){
                subjectTeacher.setSubjectTeacherId( Integer.parseInt(rs.getString("subject_teacher_id")));
                subjectTeacher.setSubjectTeacherId( Integer.parseInt(rs.getString("subject_id")));
            }
            return subjectTeacher;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

//    //-------------------------ДОБАВЛЕНИЕ ДАННЫХ-------------------------------------
//
//
    public String addAdmin(Admin newAdmin){
        String addData[] = {
//                //String.format("INSERT INTO person (name, surname, lastname, personal_phone) VALUES('%s', '%s', '%s', '%s');", newAdmin.getName(), newAdmin.getSurname(), newAdmin.getLastname(), newAdmin.getPhone()),
//                String.format("INSERT INTO user (login, password, role) VALUES('%s', '%s', '%s');", newAdmin.getLogin(), newAdmin.getPassword(), newAdmin.getRole()),
//                String.format("INSERT INTO admin (name, surname, patronymic, phone_number, email, user_id) VALUES('%s', '%s', '%s', '%s', '%s', last_insert_id());", newAdmin.getName(), newAdmin.getSurname(), newAdmin.getPatronymic(), newAdmin.getPhoneNumber(), newAdmin.getEmail())
                String.format("INSERT INTO person (name, surname, patronymic, phone, email) VALUES('%s', '%s', '%s', '%s', '%s');", newAdmin.getName(), newAdmin.getSurname(), newAdmin.getPatronymic(), newAdmin.getPhone(), newAdmin.getEmail()),
                String.format("INSERT INTO user (login, password, role, person_id) VALUES('%s', '%s', '%s', last_insert_id());", newAdmin.getLogin(), newAdmin.getPassword(), newAdmin.getRole()),
                String.format("INSERT INTO admin (rights, block, user_id) VALUES('%s', '%s', last_insert_id());", newAdmin.getRights(), newAdmin.getBlock())
        };
        return addData(addData);
    }

    public String addTeacher(Teacher teacher){
        String addData[] = {
                String.format("INSERT INTO person (surname, name, patronymic, phone, email) VALUES('%s', '%s', '%s', '%s', '%s');", teacher.getName(), teacher.getSurname(), teacher.getPatronymic(), teacher.getPhone(), teacher.getEmail()),
                String.format("INSERT INTO user (login, password, role, person_id) VALUES('%s', '%s', '%s', last_insert_id());", teacher.getLogin(), teacher.getPassword(), teacher.getRole()),
                String.format("INSERT INTO teacher (post, department, user_id) VALUES('%s', '%s', last_insert_id());", teacher.getPost(), teacher.getDepartment())
        };
        return addData(addData);
    }

    public String addStudent(Student student){
        String addData[] = {
                String.format("INSERT INTO person (surname, name, patronymic, phone, email) VALUES('%s', '%s', '%s', '%s', '%s');", student.getName(), student.getSurname(), student.getPatronymic(), student.getPhone(), student.getEmail()),
                String.format("INSERT INTO user (login, password, role, person_id) VALUES('%s', '%s', '%s', last_insert_id());", student.getLogin(), student.getPassword(), student.getRole()),
                String.format("INSERT INTO student (DOB, form_of_education, address, group_id, user_id) VALUES('%s', '%d', '%s', '%d', last_insert_id());", student.getDOB(), student.getFormOfEducation(), student.getAddress(), student.getGroupId())
        };
        return addData(addData);
    }

    public String addSubject(Subject subject){
        String addData[] = {
                String.format("INSERT INTO subject (name_of_subject) VALUES('%s');", subject.getSubjectName()),
        };
        return addData(addData);
    }

    public String addGroup(Group group){
        String addData[] = {String.format("INSERT INTO `group` (number_of_group, speciality_id) SELECT '%d', speciality.speciality_id FROM speciality WHERE speciality.name_of_speciality = '%s';", group.getNumberOfGroup(), group.getSpecialityName())};
        return addData(addData);
    }

    public String addExam(Exam exam){
        String addData[] = {
//                //String.format("INSERT INTO person (name, surname, lastname, personal_phone) VALUES('%s', '%s', '%s', '%s');", newAdmin.getName(), newAdmin.getSurname(), newAdmin.getLastname(), newAdmin.getPhone()),
//                String.format("INSERT INTO user (login, password, role) VALUES('%s', '%s', '%s');", newAdmin.getLogin(), newAdmin.getPassword(), newAdmin.getRole()),
//                String.format("INSERT INTO admin (name, surname, patronymic, phone_number, email, user_id) VALUES('%s', '%s', '%s', '%s', '%s', last_insert_id());", newAdmin.getName(), newAdmin.getSurname(), newAdmin.getPatronymic(), newAdmin.getPhoneNumber(), newAdmin.getEmail())
                String.format("INSERT INTO result (date, semester, student_id, subject_teacher_id) VALUES('%s', '%d', '%d', '%d');", exam.getDate(), exam.getSemester(), exam.getStudentId(), exam.getSubjectTeacherId()),
                String.format("INSERT INTO exam (grade, result_id) VALUES('%d', last_insert_id());", exam.getGrade())
        };
        return addData(addData);
    }
    private String addData(String[] data){
        try{
            for(int i = 0; i < data.length;i++){
                super.getStatement().execute(data[i]);
            }
            return "Успешно добавлено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось добавить данные!";
    }

    //--------------------------ОБНОВЛЕНИЕ ДАННЫХ--------------------------------------

    public String getCheckExam(Exam resultExam) throws SQLException {
            try {
                ResultSet rs = super.getStatement().executeQuery(String.format(
                        "SELECT * FROM result " +
                                "INNER JOIN student ON result.student_id = student.student_id \n" +
                                "INNER JOIN subject_teacher ON result.subject_teacher_id = subject_teacher.subject_teacher_id \n" +
                                "INNER JOIN teacher ON subject_teacher.teacher_id = teacher.teacher_id \n" +
                                "INNER JOIN user on teacher.user_id=user.user_id\n" +
                                "INNER JOIN subject ON subject_teacher.subject_id = subject.subject_id \n" +
                                "INNER JOIN exam ON result.result_id = exam.result_id \n" +
                                "INNER JOIN person on user.person_id=person.person_id WHERE exam.exam_id='%d';\n", resultExam.getId()
                ));
                while (rs.next()) {
                    String result = "";
                    result += rs.getString("result_id") + "#";
                    result += rs.getString("date") + "#";
                    result += rs.getString("semester") + "#";
                    result += rs.getString("student_id") + "#";
                    result += rs.getString("subject_teacher_id") + "#";
                    result += rs.getString("name_of_subject") + "#";
                    result += rs.getString("surname") + "#";
                    result += rs.getString("exam_id") + "#";
                    result += rs.getString("grade") + "#";
                    return result;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        return "";
    }

    public String getCheckTest(Test resultTest) throws SQLException {
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM result " +
                            "INNER JOIN student ON result.student_id = student.student_id \n" +
                            "INNER JOIN subject_teacher ON result.subject_teacher_id = subject_teacher.subject_teacher_id \n" +
                            "INNER JOIN teacher ON subject_teacher.teacher_id = teacher.teacher_id \n" +
                            "INNER JOIN user on teacher.user_id=user.user_id\n" +
                            "INNER JOIN subject ON subject_teacher.subject_id = subject.subject_id \n" +
                            "INNER JOIN test ON result.result_id = test.result_id \n" +
                            "INNER JOIN person on user.person_id=person.person_id WHERE test.test_id='%d';\n", resultTest.getId()
            ));
            while (rs.next()) {
                String result = "";
                result += rs.getString("result_id") + "#";
                result += rs.getString("date") + "#";
                result += rs.getString("semester") + "#";
                result += rs.getString("student_id") + "#";
                result += rs.getString("subject_teacher_id") + "#";
                result += rs.getString("name_of_subject") + "#";
                result += rs.getString("surname") + "#";
                result += rs.getString("test_id") + "#";
                result += rs.getString("is_passed") + "#";
                return result;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return "";
    }


    public String updateAdmin(Admin admin) throws SQLException{
        String statement = String.format("UPDATE user INNER JOIN person ON person.person_id=user.person_id \n" +
                        "INNER JOIN admin ON admin.user_id=user.user_id\n" +
                        "SET name='%s',surname='%s',patronymic='%s',phone='%s',email='%s'," +
                        "login='%s',rights='%s',block='%s'\n" +
                        " WHERE user.user_id='%d';", admin.getName(), admin.getSurname(), admin.getPatronymic(), admin.getPhone(), admin.getEmail(),
                admin.getLogin(), admin.getRights(), admin.getBlock(), admin.getUserId());
        try {
            super.getStatement().executeUpdate(statement);
            return "Успешно сохранено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось изменить данные";
    }

    public String updateTeacher(Teacher teacher) throws SQLException{
        String statement = String.format("UPDATE user INNER JOIN person ON person.person_id=user.person_id \n" +
                        "INNER JOIN teacher ON user.user_id=teacher.user_id\n" +
                        "SET name='%s',surname='%s',patronymic='%s',phone='%s',login='%s',email='%s',post='%s',department='%s'\n" +
                        "WHERE user.user_id='%d';", teacher.getName(), teacher.getSurname(), teacher.getPatronymic(), teacher.getPhone(),
                teacher.getLogin(), teacher.getEmail(), teacher.getPost(), teacher.getDepartment(), teacher.getUserId());
        return updateData(statement);
    }

    public String updateStudent(Student student) throws SQLException{
        String statement = String.format("UPDATE user INNER JOIN person ON person.person_id=user.person_id \n" +
                        "INNER JOIN student ON user.user_id=student.user_id\n" +
                        "SET name='%s',surname='%s',patronymic='%s',phone='%s',login='%s',email='%s',address='%s',DOB='%s',form_of_education='%d',group_id='%d'\n" +
                        "WHERE user.user_id='%d';", student.getName(), student.getSurname(), student.getPatronymic(), student.getPhone(),
                student.getLogin(), student.getEmail(), student.getAddress(), student.getDOB(), student.getFormOfEducation(), student.getGroupId(), student.getUserId());
        return updateData(statement);
    }

    public String updateMyUserData(User user) throws SQLException{
        String statement = String.format("UPDATE user SET login='%s',password='%s' where user_id='%d';",
                user.getLogin(), user.getPassword(), user.getId());
        try {
            ResultSet rs1 = super.getStatement().executeQuery(String.format("SELECT * from user WHERE login='%s' and user_id!='%d';", user.getLogin(), user.getId()));
            if(rs1.next()){return "Пользователь с таким логином уже существует!"; }
            super.getStatement().executeUpdate(statement);
            return "Успешно сохранено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось изменить данные";
    }

    public String updatePerson(User user) throws SQLException{
        String statement = String.format("UPDATE person inner join user on person.person_id=user.user_id SET name='%s', surname='%s', " +
                "patronymic='%s', phone='%s', email='%s' where user_id='%d';", user.getName(), user.getSurname(),user.getPatronymic(), user.getPhone(), user.getEmail(), user.getId());
        return updateData(statement);
    }

    public String updatePassword(User user){
        String statement = String.format("UPDATE user SET password='%s' WHERE user.user_id='%d';", user.getPassword(), user.getId());
        return updateData(statement);
    }

    public String updateSubject(Subject subject) {
        String statement = String.format("UPDATE subject " +
                "SET name_of_subject='%s' " +
                "WHERE subject.subject_id='%d';", subject.getSubjectName(), subject.getSubjectId());
        return updateData(statement);
    }

    public String updateGroup(Group group) {
        String statement = String.format("UPDATE `group` " +
                        "INNER JOIN speciality ON `group`.speciality_id = speciality.speciality_id " +
                        "SET `group`.number_of_group = '%d', `group`.speciality_id = '%d' " +
                        "WHERE `group`.group_id = '%d' AND speciality.name_of_speciality = '%s';",
                group.getNumberOfGroup(), group.getSpecialityId(), group.getGroupId(), group.getSpecialityName());
        return updateData(statement);
    }


    private String updateData(String statement){
        try {
            super.getStatement().executeUpdate(statement);
            return "Успешно сохранено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось изменить данные";
    }

//    //------------------------------УДАЛЕНИЕ ДАННЫХ----------------------------------
//
//
    public String deleteAdmin(Admin deleteAdmin){
        String statement = String.format("DELETE from admin, user, person " +
                "USING admin, user, person " +
                "WHERE user.user_id=admin.user_id && user.person_id=person.person_id && user.login='%s';", deleteAdmin.getLogin());
        try{
            super.getStatement().execute(statement);
            return "Успешно удалено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось удалить данные!";
    }

    public String deleteTeacher(Teacher deleteTeacher){
        String statement = String.format("DELETE from person, teacher, user USING person, teacher, user WHERE person.person_id=user.person_id and user.user_id=teacher.user_id and teacher.teacher_id='%d';", deleteTeacher.getId());
        try{
            super.getStatement().execute(statement);
            return "Успешно удалено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось удалить данные!";
    }
//
public String deleteSubject(Subject subject) {
    String statement = String.format("DELETE FROM subject WHERE subject.subject_id = %d;", subject.getSubjectId());
    try {
        super.getStatement().execute(statement);
        return "Успешно удалено!";
    } catch (Exception ex) {
        System.out.println(ex.getMessage());
    }
    return "Не удалось удалить данные!";
}

    public String deleteStudent(Student student){
        String statement = String.format("DELETE from person, student, user USING person, student, user WHERE person.person_id=user.person_id and user.user_id=student.user_id and student.student_id='%d';", student.getId());
        try{
            super.getStatement().execute(statement);
            return "Успешно удалено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось удалить данные!";
    }

    public String deleteGroup(Group group){
        String statement = String.format("DELETE FROM `group`  WHERE `group`.group_id = '%d';", group.getGroupId());
        try{
            super.getStatement().execute(statement);
            return "Успешно удалено!";
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return "Не удалось удалить данные!";
    }

    public  ArrayList<Student> findStudents(Student findStudent){
        try {
            ResultSet rs = super.getStatement().executeQuery(String.format(
                    "SELECT * FROM user " +
                            "INNER JOIN person ON user.person_id = person.person_id \n" +
                            "INNER JOIN student ON user.user_id = student.user_id \n" +
                            "INNER JOIN `group` ON student.group_id = `group`.group_id \n" +
                            "INNER JOIN speciality ON `group`.speciality_id = speciality.speciality_id \n" +
                            "INNER JOIN faculty ON speciality.faculty_id = faculty.faculty_id \n" +
                            "WHERE `group`.number_of_group = '%d';", findStudent.getNumberOfGroup())
            );
            ArrayList<Student> studentList = new ArrayList<>();
            while(rs.next()){
                Student student = new Student(
                        Integer.parseInt(rs.getString("person_id")),
                        rs.getString("surname"),
                        rs.getString("name"),
                        rs.getString("patronymic"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        Integer.parseInt(rs.getString("user_id")),
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("role"),
                        Integer.parseInt(rs.getString("student_id")),
                        rs.getString("DOB"),
                        Integer.parseInt(rs.getString("form_of_education")),
                        rs.getString("address"),
                        Integer.parseInt(rs.getString("group_id")),
                        Integer.parseInt(rs.getString("number_of_group")),
                        Integer.parseInt(rs.getString("faculty_id")),
                        rs.getString("name_of_faculty"),
                        Integer.parseInt(rs.getString("speciality_id")),
                        rs.getString("name_of_speciality")
                );
                studentList.add(student);
            }
            return studentList;
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
