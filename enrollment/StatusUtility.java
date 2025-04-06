package com.example.lms.enrollment;

public class StatusUtility {

    public static String getStatusBasedOnProgress(double progress) {
        if (progress == 0.0) {
            return "ENROLLED";
        } else if (progress < 100.0) {
            return "IN_PROGRESS";
        } else {
            return "COMPLETED";
        }
    }
}