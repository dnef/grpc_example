package com.sbt.grpc;

import com.google.protobuf.TextFormat;
import com.sbt.grpc.type.RequestEx;
import com.sbt.grpc.type.ResponseEx;
import com.sbt.grpc.type.ServiceExGrpc;
import com.sbt.grpc.type.UserData;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.net.ConnectException;
import java.time.LocalDate;

public class ManagerUserForGrpc {
    private static ManagedChannel channel ;
    static ResponseEx getUserData(final String login) {
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                    .usePlaintext()
                    .build();
//            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
//                    .usePlaintext()
//                    .build();
            ServiceExGrpc.ServiceExBlockingStub stub = ServiceExGrpc.newBlockingStub(channel);
            //UserData.Builder userData = UserData.newBuilder();
            ResponseEx responseEx = stub.executeGrpc(RequestEx.newBuilder()
                    .setLogin(login)
                    .build());
//            channel.shutdown();
            return responseEx;
        } catch (RuntimeException exception) {
            ResponseEx responseEx = ResponseEx.newBuilder().setError("Error server").build();
            return responseEx;
        }finally {
            channel.shutdown();
        }

    }

    static ResponseEx setEmailByUser(final String login, final String email) {
        try {
            channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                    .usePlaintext()
                    .build();
//            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
//                    .usePlaintext()
//                    .build();
            ServiceExGrpc.ServiceExBlockingStub stub = ServiceExGrpc.newBlockingStub(channel);

            UserData.Builder userData = UserData.newBuilder();
            ResponseEx responseEx = stub.updateGrpc(RequestEx.newBuilder()
                    .setLogin(login)
                    .setUserData(userData.setEmail(email))
                    .build());
//            channel.shutdown();
            return responseEx;
        } catch (RuntimeException exception) {
            ResponseEx responseEx = ResponseEx.newBuilder().setError("Error server").build();
            return responseEx;
        }finally {
            channel.shutdown();
        }
    }
}
