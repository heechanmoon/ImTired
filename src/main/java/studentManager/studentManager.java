package studentManager;

import view.StudentView;

import java.util.Scanner;

public class studentManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        StudentView studentViewer = new StudentView(scanner);

        studentViewer.showMenu();
    }
}
