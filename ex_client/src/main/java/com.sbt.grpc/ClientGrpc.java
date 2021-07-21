package com.sbt.grpc;

import com.google.protobuf.TextFormat;
import com.sbt.grpc.type.ResponseEx;

import java.time.LocalDate;
import java.util.Scanner;

public class ClientGrpc {
    public static void main(String[] args) {
        System.out.println("1. Вывод данных\n2. Изменение Email\n3. Exit");
        Scanner scanner = new Scanner(System.in);
        String action;
        String login;
        String email;
        while (true) {
            System.out.print("Действие :");
            action = scanner.nextLine();
            switch (action) {
                case "1":
                    System.out.print("input login :");
                    login = scanner.nextLine();
                    ResponseEx responseEx = ManagerUserForGrpc.getUserData(login);
                    User user = null;
                    if (responseEx.getError().equals("")) {
                        user = new User();
                        user.setLogin(responseEx.getLogin());
                        user.setFirstName(responseEx.getUserData().getFirstName());
                        user.setLastName(responseEx.getUserData().getLastName());
                        user.setDateOfBirth(LocalDate.parse(responseEx.getUserData().getDateofBirth()));
                        user.setEmail(responseEx.getUserData().getEmail());
                    }
                    System.out.println(user);
                    //System.out.println("Response client : "+TextFormat.printToUnicodeString(responseEx));
                    System.out.println(TextFormat.printer().escapingNonAscii(false).printToString(responseEx));
                    break;
                case "2":
                    System.out.print("input login :");
                    login = scanner.nextLine();
                    System.out.print("new email :");
                    email = scanner.nextLine();
                    ResponseEx responseExAddEmail = ManagerUserForGrpc.setEmailByUser(login, email);
                    System.out.println(TextFormat.printer().escapingNonAscii(false).printToString(responseExAddEmail));
                    break;
                case "3":
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Не корректные данные");
            }

        }
    }
}
