package view;

import model.Student;
import util.ScannerUtil;
import controller.StudentController;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StudentView {
    private StudentController studentController;
    private final Scanner SCANNER;

    public StudentView(Scanner scanner){
        SCANNER = scanner;
        studentController = new StudentController();
    }

    public void showMenu(){
        String message = "1. 입력 2. 목록보기 3. 종료";
        while(true){
            int userChoice = ScannerUtil.nextInt(SCANNER, message);
            if(userChoice == 1){
                insert();
            } else if (userChoice == 2) {
                printList();
            } else if (userChoice == 3) {
                try {
                    studentController.terminate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                System.out.println("감사합니다.");
                break;
            }
        }
    }

    //insert()
    private void insert(){
        Student s = new Student();

        String query = "insert into `student` (`name`, `korean`, `english`, `math`) values(?,?,?,?)";

        String message = "이름 입력 : ";
        s.setName(ScannerUtil.nextLine(SCANNER, message));

        message = "국어 점수 입력 : ";
        s.setKorean(ScannerUtil.nextInt(SCANNER, message));

        message = "영어 점수 입력 : ";
        s.setEnglish(ScannerUtil.nextInt(SCANNER, message));

        message = "수학 점수 입력 : ";
        s.setMath(ScannerUtil.nextInt(SCANNER, message));

        studentController.insert(query,s);
    }

    private void printList() {

        ArrayList<Student> list = studentController.selectAll();

        if (list.isEmpty()) {
            System.out.println("아직 등록된 학생이 없습니다.");
        } else {
            for (Student s : list) {
                System.out.printf("%d. %s\n", s.getId(), s.getName());
            }

            String message = "상세보기할 학생의 번호를 입력해주세요. ( 뒤로 가기 : 0 )";
            int userChoice = ScannerUtil.nextInt(SCANNER, message);

            while (userChoice != 0 && studentController.selectOne(userChoice) == null) {
                System.out.println("잘못 입력하셨습니다.");
                userChoice = ScannerUtil.nextInt(SCANNER, message);
            }

            if (userChoice != 0) {
                printOne(userChoice);
            }
        }
    }

    private void printOne(int id){
        Student s = studentController.selectOne(id);
        s.printInfo();

        String message = "1.수정 2. 삭제 3. 뒤로가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1,3);

        if(userChoice == 1){
            update(id);
        } else if (userChoice == 2) {
            delete(id);
        } else if (userChoice == 3) {
            printList();
        }
    }

    private void update(int id){
        String message = "새 국어 점수 : ";
        int korean = ScannerUtil.nextInt(SCANNER,message,0,100);

        message = "새 영어 점수 : ";
        int english = ScannerUtil.nextInt(SCANNER,message,0,100);

        message = "새 수학 점수 : ";
        int math = ScannerUtil.nextInt(SCANNER,message,0,100);

        studentController.update(id,korean,english,math);

        printOne(id);
    }

    private void delete(int id) {
        String message = "정말 삭제하시겠습니까? Y/N";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        if (yesNo.equalsIgnoreCase("y")) {
            studentController.delete(id);
            printList();
        } else {
            printOne(id);
        }
    }
}
