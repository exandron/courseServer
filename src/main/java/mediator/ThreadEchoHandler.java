package mediator;

import db.DAO;
import model.*;

import javax.print.Doc;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ThreadEchoHandler implements Runnable{
    Socket clientSocket = null;
    ObjectInputStream input;
    ObjectOutputStream output;

    public ThreadEchoHandler(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());
        }
        catch(Exception e) {

        }
    }

    @Override
    public void run() {
        try {
            DAO dao = new DAO();
            String command = "";
            while(!command.equals("exit")){
                command = (String) input.readObject();
               switch (command){
                   case "authorization":
                        User authorizationUser = (User) input.readObject();
                        output.writeObject(dao.authorization(authorizationUser));
                        break;
                    case "getAllAdmins":
                        output.writeObject(dao.getAllAdmins());
                        break;
                    case "getAllTeachers":
                        output.writeObject(dao.getAllTeachers());
                        break;
                    case "getAllStudents":
                        output.writeObject(dao.getAllStudents());
                        break;
                   case "getAllExams":
                       Exam exam = (Exam) input.readObject();
                       output.writeObject(dao.getAllExams(exam));
                       break;
                   case "getAllTests":
                       Test test = (Test) input.readObject();
                       output.writeObject(dao.getAllTests(test));
                       break;
                   case "getAllSubjects":
                       output.writeObject(dao.getAllSubjects());
                       break;
                   case "getGroup":
                       Group group = (Group) input.readObject();
                       output.writeObject(dao.getGroup(group));
                       break;
                   case "getSpeciality":
                       Speciality speciality= (Speciality) input.readObject();
                       output.writeObject(dao.getSpeciality(speciality));
                       break;
                   case "getAllGroups":
                       output.writeObject(dao.getAllGroups());
                       break;
                   case "getAllSpecialities":
                       output.writeObject(dao.getAllSpecialities());
                       break;
                   case "getAllSubjectTeacher":
                       Teacher teacher = (Teacher) input.readObject();
                       output.writeObject(dao.getAllSubjectTeacher(teacher));
                       break;
                    case "insertAdmin":
                        Admin newAdmin = (Admin) input.readObject();
                        output.writeObject(dao.addAdmin(newAdmin));
                        break;
                    case "insertTeacher":
                        Teacher newTeacher = (Teacher) input.readObject();
                        output.writeObject(dao.addTeacher(newTeacher));
                        break;
                    case "insertStudent":
                        Student newStudent = (Student) input.readObject();
                        output.writeObject(dao.addStudent(newStudent));
                        break;
                    case "insertSubject":
                        Subject newSubject = (Subject) input.readObject();
                        output.writeObject(dao.addSubject(newSubject));
                        break;
                   case "insertGroup":
                       Group newGroup = (Group) input.readObject();
                       output.writeObject(dao.addGroup(newGroup));
                       break;
                   case "insertExamResult":
                       Exam addExam= (Exam) input.readObject();
                       output.writeObject(dao.addExam(addExam));
                       break;
                    case "updateMyUserData":
                        User updateMyUserData = (User) input.readObject();
                        output.writeObject(dao.updateMyUserData(updateMyUserData));
                        break;
                    case "updatePassword":
                        User updatePassword = (User) input.readObject();
                        output.writeObject(dao.updatePassword(updatePassword));
                        break;
                    case "updatePerson":
                        User updatePerson = (User) input.readObject();
                        output.writeObject(dao.updatePerson(updatePerson));
                        break;
                    case "updateAdmin":
                        Admin updateAdmin = (Admin) input.readObject();
                        output.writeObject(dao.updateAdmin(updateAdmin));
                        break;
                    case "updateTeacher":
                        Teacher updateTeacher = (Teacher) input.readObject();
                        output.writeObject(dao.updateTeacher(updateTeacher));
                        break;
                    case "updateStudent":
                        Student updateStudent = (Student) input.readObject();
                        output.writeObject(dao.updateStudent(updateStudent));
                        break;
                    case "updateSubject":
                        Subject updateSubject = (Subject) input.readObject();
                        output.writeObject(dao.updateSubject(updateSubject));
                        break;
                   case "updateGroup":
                       Group updateGroup = (Group) input.readObject();
                       output.writeObject(dao.updateGroup(updateGroup));
                       break;
                    case "deleteAdmin":
                        Admin deleteAdmin = (Admin) input.readObject();
                        output.writeObject(dao.deleteAdmin(deleteAdmin));
                        break;
                    case "deleteTeacher":
                        Teacher deleteTeacher = (Teacher) input.readObject();
                        output.writeObject(dao.deleteTeacher(deleteTeacher));
                        break;
                    case "deleteSubject":
                        Subject deleteSubject = (Subject) input.readObject();
                        output.writeObject(dao.deleteSubject(deleteSubject));
                        break;
                    case "deleteStudent":
                        Student deleteStudent = (Student) input.readObject();
                        output.writeObject(dao.deleteStudent(deleteStudent));
                        break;
                   case "deleteGroup":
                       Group deleteGroup = (Group) input.readObject();
                       output.writeObject(dao.deleteGroup(deleteGroup));
                       break;
                   case "findStudentsByGroupNumber":
                       Student findStudent = (Student) input.readObject();
                       output.writeObject(dao.findStudents(findStudent));
                       break;
                   case "getSubjectTeacher":
                       SubjectTeacher subjectTeacher = (SubjectTeacher) input.readObject();
                       output.writeObject(dao.getSubjectTeacher(subjectTeacher));
                       break;
                   case "getCheckExam":
                       Exam resultExam = (Exam) input.readObject();
                       output.writeObject(dao.getCheckExam(resultExam));
                       break;
                   case "getCheckTest":
                       Test resultTest = (Test) input.readObject();
                       output.writeObject(dao.getCheckTest(resultTest));
                       break;
                   default: System.out.println("Нет такой команды");
               }
            }
        }
        catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            System.out.println("Закрыто подключение...\nКоличество активных подключений: " + --Mediator.connectionsCounter + "\n");
        }
    }
}
