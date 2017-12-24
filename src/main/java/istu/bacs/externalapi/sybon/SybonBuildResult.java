package istu.bacs.externalapi.sybon;

import lombok.Data;

@Data
class SybonBuildResult {
    private Status status;
    private String output;

    enum Status {
        OK,
        FAILED,
        PENDING,
        SERVER_ERROR
    }
}