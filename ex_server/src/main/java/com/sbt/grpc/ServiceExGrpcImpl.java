package com.sbt.grpc;


import com.sbt.grpc.type.RequestEx;
import com.sbt.grpc.type.ResponseEx;
import com.sbt.grpc.type.ServiceExGrpc;
import com.sbt.grpc.type.UserData;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.logging.Logger;


public class ServiceExGrpcImpl extends ServiceExGrpc.ServiceExImplBase {
    public static HashMap<String, User> users = new HashMap<>();
    Logger logger = Logger.getLogger(ServiceExGrpcImpl.class.getName());

    public ServiceExGrpcImpl() {
        addUsers();
    }

    @Override
    public void executeGrpc(RequestEx requestEx, StreamObserver<ResponseEx> responseExStreamObserver) {
        logger.info("Request in server executeGrpc:" + requestEx);

        String bodyRequestLogin = new StringBuilder()
                .append(requestEx.getLogin())
                .toString();
        try {
            User user = getUser(bodyRequestLogin);
            ResponseEx responseEx = ResponseEx.newBuilder()
                    .setLogin(bodyRequestLogin)
                    .setUserData(UserData.newBuilder()
                            .setFirstName(user.getFirstName())
                            .setLastName(user.getLastName())
                            .setDateofBirth(user.getDateOfBirth().toString())
                            .setEmail(user.getEmail())
                            .build())
                    .build();
            responseExStreamObserver.onNext(responseEx);
        } catch (IllegalArgumentException exception) {
            logger.info(exception.getMessage());
            ResponseEx responseEx = ResponseEx.newBuilder().setError(exception.getMessage()).build();
            responseExStreamObserver.onNext(responseEx);
        }
        responseExStreamObserver.onCompleted();
    }

    @Override
    public void updateGrpc(RequestEx requestEx, StreamObserver<ResponseEx> responseExStreamObserver) {
        logger.info("Request in server updateGrpc:" + requestEx);
        String bodyRequestLogin = new StringBuilder()
                .append(requestEx.getLogin())
                .toString();
        String email = new StringBuilder()
                .append(requestEx.getUserData().getEmail())
                .toString();
        try {
            User user = getUser(bodyRequestLogin);
            user.setEmail(email);
            users.put(user.getLogin(), user);
            ResponseEx responseEx = ResponseEx.newBuilder()
                    .setLogin(bodyRequestLogin)
                    .setUserData(UserData.newBuilder()
//                        .setFirstName(user.getFirstName())
//                        .setLastName(user.getLastName())
//                        .setDateofBirth(user.getDateOfBirth().toString())
                            .setEmail(user.getEmail())
                            .build())
                    .build();
            responseExStreamObserver.onNext(responseEx);
        } catch (IllegalArgumentException exception) {
            logger.info(exception.getMessage());
            ResponseEx responseEx = ResponseEx.newBuilder().setError(exception.getMessage()).build();
            responseExStreamObserver.onNext(responseEx);
        }
        responseExStreamObserver.onCompleted();

    }

    private HashMap<String, User> addUsers() {
        User petrov = new User("petrov", "Петров", "Петр", LocalDate.of(1990, 12, 12), "petrov@sbrf.ru");
        User ivanov = new User("ivanov", "Иванов", "Иван", LocalDate.of(1991, 11, 11), "ivanov@sbrf.ru");
        User sidorov = new User("sidorov", "Сидоров", "Василий", LocalDate.of(1992, 10, 10), "sidorov@sbrf.ru");
        users.put(petrov.getLogin(), petrov);
        users.put(ivanov.getLogin(), ivanov);
        users.put(sidorov.getLogin(), sidorov);
        return users;
    }

    private User getUser(String login) throws IllegalArgumentException {
        if (users.get(login) == null || users.get(login).equals(null)) {
            throw new IllegalArgumentException("Not found " + login);
        }
        return users.get(login);
    }

}

