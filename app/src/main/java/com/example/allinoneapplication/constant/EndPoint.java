package com.example.allinoneapplication.constant;

public class EndPoint {

    //Live Paths
// public static final String BASE_URL = "https://savage-absence.000webhostapp.com/AllInOneAPI/";
// public static final String IMAGE_URL = "https://savage-absence.000webhostapp.com/AllInOneAPI/api";

    //Local Paths
  public static final String BASE_URL = "http://192.168.0.109/AllInOneAPI/";
  public static final String IMAGE_URL = "http://192.168.0.109/AllInOneAPI/api";

    public static final String MAP_URL = "https://maps.googleapis.com/maps/api/";

    public static final String NEARBY_PLACES = MAP_URL + "place/nearbysearch/json?";
    public static final String CUSTOMER_SIGNUP_URL = BASE_URL + "api/CreateCustomerAccountAPI.php";
    public static final String MECHANIC_SIGNUP_URL = BASE_URL + "api/CreateMechanicAccountAPI.php";
    public static final String WORKSHOP_SIGNUP_URL = BASE_URL + "api/CreateWorkshopMechanicAccountAPI.php";
    public static final String CUSTOMER_LOGIN_URL = BASE_URL + "api/CustomerLoginAPI.php";
    public static final String MECHANIC_LOGIN_URL = BASE_URL + "api/MechanicLoginAPI.php";
    public static final String WORKSHOP_LOGIN_URL = BASE_URL + "api/WorkshopLoginAPI.php";
    public static final String NEARBY_AREA_URL = BASE_URL + "api/CreateNearbyAPI.php";
    public static final String GET_NEARBY_AREA_URL = BASE_URL + "api/GetNearbyAreasAPI.php";
    public static final String GET_MANAGE_USER_DETAILS_URL = BASE_URL + "api/GetManageUser.php";
    public static final String GET_MANAGE_MECHANIC_DETAILS_URL = BASE_URL + "api/GetManageMechanicAPI.php";
    public static final String GET_MANAGE_WORKSHOP_DETAILS_URL = BASE_URL + "api/GetManageWorkshopAPI.php";
    public static final String BOOKING_DETAILS_URL = BASE_URL + "api/CreateBookingAPI.php";
    public static final String CREATE_SERVICE_URL = BASE_URL + "api/CreateServiceAPI.php";
    public static final String GET_SERVICE_URL = BASE_URL + "api/GetServicesAPI.php";
    public static final String GET_MECHANIC_BOOK_URL = BASE_URL + "api/GetMechanicBookingAPI.php";
    public static final String UPDATE_BOOK_STATUS_URL = BASE_URL + "api/UpdatBookingStatusAPI.php";
    public static final String ADMIN_LOGIN_URL = BASE_URL + "api/AdminLoginAPI.php";
    public static final String UPDATE_MECHANIC_STATUS_URL = BASE_URL + "api/UpdateMechanicStatusAPI.php";
    public static final String UPDATE_SERVICE_URL = BASE_URL + "api/UpdateServiceAPI.php";
    public static final String UPDATE_SERVICE_STATUS_URL = BASE_URL + "api/UpdateServiceStatusAPI.php";
    public static final String INSERT_UPDATE_LOC_URL = BASE_URL + "api/InsertUpdateLocationAPI.php";
    public static final String GET_MECHANIC_LOC_URL = BASE_URL + "api/GetMechanicLocationAPI.php";
    public static final String GET_CUSTOMER_BOOK_URL = BASE_URL + "api/GetBookingCustomerAPI.php";
    public static final String UPDATE_BOOK_URL = BASE_URL + "api/UpdateBookStatusAPI.php";
    public static final String SEND_CHAT_MESSAGE = BASE_URL + "api/SendMessageAPI.php";
    public static final String GET_CHAT_MESSAGE = BASE_URL + "api/GetMessageAPI.php";
    public static final String GET_CUSTOMER_LOC = BASE_URL + "api/GetCustomerLocationAPI.php";
    public static final String REGISTER_COMPLAIN_URL = BASE_URL + "api/RegisterComplainAPI.php";
    public static final String GET_COMPLAIN_URL = BASE_URL + "api/GetAdminComplaintAPI.php";
    public static final String GET_COMPLAINT_DETAIL = BASE_URL + "api/GetComplainAPI.php";
    public static final String UPDATE_CUSTOMER_PROFILE = BASE_URL + "api/UpdateUserProfileAPI.php";
    public static final String UPDATE_MECHANIC_PROFILE = BASE_URL + "api/UpdateMechanicProfileAPI.php";
    public static final String UPDATE_CUSTOMER_IMAGE = BASE_URL + "api/UpdateCustomerImageAPI.php";
    public static final String UPDATE_MECHANIC_IMAGE = BASE_URL + "api/UpdatemechanicimageAPI.php";
    public static final String CUSTOMER_CHANGE_PASSWORD = BASE_URL + "api/ChangePasswordAPI.php";
    public static final String MECHANIC_CHANGE_PASSWORD = BASE_URL + "api/changepasswordmechanicAPI.php";
    public static final String UPDATE_COMPLAIN_STATUS = BASE_URL + "api/UpdateComplaintStatusAPI.php";
    public static final String WORKSHOP_MECHANIC_CHANGE_PASSWORD = BASE_URL + "api/changeworkshopmechanicpasswordAPI.php";
    public static final String UPDATE_WORKSHOP_MECHANIC_IMAGE = BASE_URL + "api/updateworkshopMechanicimageAPI.php";
    public static final String UPDATE_WORKSHOP_MECHANIC_PROFILE = BASE_URL + "api/UpdateWorkshopMechanicProfile.php";
    public static final String ADD_WORKSHOP_MECHANIC = BASE_URL + "api/AddWorkshopMechanicAPI.php";
    public static final String UPDATE_ADMIN_PROFILE = BASE_URL + "api/UpdateAdminProfileAPI.php";
    public static final String UPDATE_WORKSHOP_MECHANIC_STATUS_URL = BASE_URL + "api/UpdateWorkshopmechanicstatusAPI.php";
    public static final String UPDATE_CUSTOMER_STATUS_URL = BASE_URL + "api/UpdateCustomerStatusAPI.php";
    public static final String INSERT_FAVOURITE_URL = BASE_URL + "api/InsertFavouriteAPI.php";
    public static final String GET_MECH_FAVOURITE_URL = BASE_URL + "api/GetFavMechanicAPI.php";
    public static final String GET_WORKSHOP_MECHANIC_URL = BASE_URL + "api/GetWorkshopMechanicAPI.php";
    public static final String GET_FAV_WORKSHOP_URL = BASE_URL + "api/GetFavWorkshopAPI.php";
    public static final String DELETE_WORKSHOP_MECHANIC_URL = BASE_URL + "api/DeleteWorkshopMechanicAPI.php";
    public static final String GET_WORKSHOP_MECH_COMPLAIN = BASE_URL + "api/GetWorkshopMechComplaintAPI.php";
    public static final String GET_CUST_RATING = BASE_URL + "api/GetCustomerRatingAPI.php";
    public static final String INSERT_RATING = BASE_URL + "api/InsertRatingAPI.php";
    public static final String GET_RATING_URL = BASE_URL + "api/GetRatingApI.php";
    public static final String GET_TOP_MECH_URL = BASE_URL + "api/GetTopMechanicAPI.php";
    public static final String GET_BOOK_REPORT_URL = BASE_URL + "api/GetReportBookingAPI.php";
    public static final String GET_CURRENT_LOCATION_URL = BASE_URL + "api/GetCurrentLocationAPI.php";
    public static final String GET_CUSTOMER_COMPLAIN_DETAIL_URL = BASE_URL + "api/GetCustomerComplaindetail.php";
    public static final String GET_MECHANIC_COMPLAIN_DETAIL_URL = BASE_URL + "api/GetMechanicComplainDetails.php";
    public static final String UPDATE_CUSTOMER_CANCEL_BOOKING_URL = BASE_URL + "api/UpdateCancelBookingAPI.php";
    public static final String DELETE_COMPLAIN_URL = BASE_URL + "api/DeleteComplainsAPI.php";
    public static final String GET_COMPLAIN_REPORT_URL = BASE_URL + "api/GetComplainReportAPI.php";
    public static final String GET_FEEDBACK_REPORT_URL = BASE_URL + "api/GetFeedbackReportAPI.php";
    public static final String GET_BOOK_RATE_URL = BASE_URL + "api/GetBookedRateAPI.php";
}



