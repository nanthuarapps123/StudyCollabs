package com.bma.studycollabs;

/**
 * Created by NANTHA on 9/15/2017.
 */

public class Constant {

        public static String root = "http://studycollab.com/";
        public static String BaseUrl = root+"mobile/api/index.php/";
        public static String BaseUrl1 = root+"api/v1/index.php/";

        public static final String IMAGE_UPLOAD_URL = BaseUrl+"Profile/profileImage";
        //Account url
        public static final String SIGN_IN_URL = BaseUrl+"Account/login";
        public static final String SIGN_UP_URL = BaseUrl+"Account/register";
        public static final String SEARCHQUESTION = BaseUrl+"Dashboard/searchQuestion";
        public static final String VIEWQUESTION = BaseUrl+"Dashboard/Viewquestion";
        public static final String POST_QUESTION = BaseUrl+"Dashboard/Postquestion";
        public static final String POSTCOMMAND = BaseUrl1+"Dashboard/addComment";
        public static final String RATINGCOMMENT = BaseUrl1+"Dashboard/ratingComment";
        public static final String FORGET_PASS_URL = BaseUrl1+"Account/forgotPassword";
        public static final String PROFILE_SCREEN_URL = BaseUrl+"Profile/profile";
        public static final String PROFILE_UPDATE_URL = BaseUrl+"Profile/updateprofileinfo";
        public static final String CHANGE_PASSWORD = BaseUrl1+"Profile/changePassword";
        public static final String VOLUNTEER_HOURS = BaseUrl1+"Dashboard/getRatingVolunteerHours";

        public static final String SCHEDULENOTIFICATION = BaseUrl+"Dashboard/getQuestionNotify";

        public static final String SUBJECTS = BaseUrl+"Dashboard/subjects";
        public static final String TUTORTYPE = BaseUrl+"Dashboard/getTutortype";
        public static final String SUBCAT = BaseUrl+"Dashboard/getSubCategory";
        public static final String SUB_GRADES = BaseUrl+"Dashboard/subjectGrades";
        public static final String SUB_SAVE_GRADES = BaseUrl+"Grading/saveGrades";

        public static final String TUTORUSER = BaseUrl+"Dashboard/getTutoruser";
        public static final String GETSCHEDULEEVENT = BaseUrl1+"Schedule/getScheduleEvent";
        public static final String SUBMITSCHEDULEEVENT = BaseUrl1+"Schedule/submitSchedule";

        public static final String APPROVEAPPOINMENT = BaseUrl1+"Schedule/approveAppointment";
        public static final String GET_REQ_SCHEDULE = BaseUrl1+"Schedule/getRequestSchedule";
        public static final String GETSCHEDULEEVENTBYUSER = BaseUrl1+"Schedule/getScheduleEventByUser";
        public static final String GETRESOURCES = BaseUrl+"Dashboard/resources";

}


