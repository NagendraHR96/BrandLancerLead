package com.example.brandlancerlead.brandUtility;

import com.example.brandlancerlead.model.ApprovedProjectsJsonResult;
import com.example.brandlancerlead.model.DPPendingDetailsJsonResult;
import com.example.brandlancerlead.model.DPPendingListJsonResult;
import com.example.brandlancerlead.model.EmailSendJsonResult;
import com.example.brandlancerlead.model.FeedbackFilter;
import com.example.brandlancerlead.model.FeedbackSendJsonResult;
import com.example.brandlancerlead.model.FollowSiteJsonObject;
import com.example.brandlancerlead.model.FromPlaceUpdate;
import com.example.brandlancerlead.model.GetAllCallStatusJsonResult;
import com.example.brandlancerlead.model.GetLeadCallFeedbackJsonResult;
import com.example.brandlancerlead.model.HoldUnHold;
import com.example.brandlancerlead.model.InsertBookingCallFeedbackFilter;
import com.example.brandlancerlead.model.InsertInprojectDetailsFilter;
import com.example.brandlancerlead.model.JsonModelObject;
import com.example.brandlancerlead.model.LeadJsonObject;
import com.example.brandlancerlead.model.LeadReportListJsonResult;
import com.example.brandlancerlead.model.LeadSubmitPostObject;
import com.example.brandlancerlead.model.LocationObject;
import com.example.brandlancerlead.model.LoginRespons;
import com.example.brandlancerlead.model.PaymentsListJsonResult;
import com.example.brandlancerlead.model.ProjectsJsonResult;
import com.example.brandlancerlead.model.RawReportSummary;
import com.example.brandlancerlead.model.RejectionJson;
import com.example.brandlancerlead.model.SiteStatusJsonResult;
import com.example.brandlancerlead.model.StatusResultJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WebContentsInterface   {

    @GET("ExecutiveLogin")
    Call<LoginRespons> userLoginCall(@Query("EmployeeId") String employeeID);

    @GET("GetLeads")
    Call<LeadJsonObject> userLeadCall(@Query("ExecutiveId") String employeeID,@Query("StatusID") String id,@Query("LeadPurposeID")String leadPurposeId);

    @GET("GetFollowUpLeads")
    Call<FollowSiteJsonObject> userFollowSiteCall(@Query("ExecutiveId") String employeeID, @Query("StatusID") String id);

    @GET("ReAssignLead")
    Call<JsonModelObject> createReFixLead(@Query("LeadID") String leadId);

    @POST("UpdateLeadOnFromplace")
    Call<JsonModelObject> leadStartCall(@Body FromPlaceUpdate fromPlace);

    @POST("UpdateLeadOnHoldFlag")
    Call<JsonModelObject> leadOnHold(@Body HoldUnHold hold);

    @GET("GetAllLeadStatus")
    Call<StatusResultJson> leadStatusCall();

    @GET("GetAllLeadStatus")
    Call<StatusResultJson> leadStatusCalls(@Query("ActivityID")String keyVal);


    @GET("GetAllRejectionReason")
    Call<RejectionJson> rejectionStates();

    @POST("UpdateLead")
    Call<JsonModelObject> submitLead(@Body LeadSubmitPostObject postLead);

    @POST("InsertLatAndLong")
    Call<JsonModelObject> sendLatiLongi(@Body LocationObject local);

    @POST("InsertLeadFeedback")
    Call<FeedbackSendJsonResult>sendFeedback(@Body FeedbackFilter filter);

    @GET("GetDPPendingList")
    Call<DPPendingListJsonResult>bindDpPending(@Query("ExecutiveId")String executiveId,@Query("IsFollowUp")boolean IsFollowUp);

    @GET("GetDPPendingDetails")
    Call<DPPendingDetailsJsonResult>bindPendingDetails(@Query("BookingID")String bookingID);

    @GET("GetPaymentsList")
    Call<PaymentsListJsonResult>bindPayments(@Query("BookingID")String bookingID);

    @GET("SendDPIntemationMail")
    Call<EmailSendJsonResult>sendMail(@Query("BookingID")String bookingID);

    @GET("GetZeroPaymentList")
    Call<DPPendingListJsonResult>bindZeroPayments(@Query("ExecutiveId")String executiveId,@Query("IsFollowUp")boolean IsFollowUp);

    @GET("GetZeroPaymentDetails")
    Call<DPPendingDetailsJsonResult>zeroPaymentDeatils(@Query("BookingID")String bookingID);

    @GET("GetDPClosedList")
    Call<DPPendingListJsonResult>bindDpClosedPayments(@Query("ExecutiveId")String executiveId,@Query("IsFollowUp")boolean IsFollowUp);

    @GET("GetDPClosedDetails")
    Call<DPPendingDetailsJsonResult>dpPaymentDeatils(@Query("BookingID")String bookingID);

    @GET("GetAllCallStatus")
    Call<GetAllCallStatusJsonResult>bindCallStatus();

    @POST("InsertBookingCallFeedback")
    Call<JsonModelObject>sendBookingCallFeedback(@Body InsertBookingCallFeedbackFilter filter);

    @GET("CheckHoldLead")
    Call<JsonModelObject>checkHoldlead(@Query("ExecutiveID")String executiveId);

    @GET("GetLeadCallFeedback")
    Call<GetLeadCallFeedbackJsonResult>bindCallDetails(@Query("LeadID")String leadID);

    @GET("GetBookingCallFeedback")
    Call<GetLeadCallFeedbackJsonResult>bindBookingCallDetails(@Query("BookingID")String bookingID);

    @GET("GetLeadReportList")
    Call<LeadReportListJsonResult>bindLeadReport(@Query("FromDate")String fromDate, @Query("ToDate")String toDate, @Query("ExecutiveID")String executiveID);

    @GET("GetLeadSummaryReport")
    Call<RawReportSummary> bindLeadsummaryReport(@Query("FromDate")String fromDate, @Query("ToDate")String toDate, @Query("ExecutiveID")String executiveID);

    @POST("InsertInprojectDetails")
    Call<LeadReportListJsonResult>insertSiteProgress(@Body InsertInprojectDetailsFilter filter);



    ///
    @GET("GetSiteStatus")
    Call<SiteStatusJsonResult> siteStatus(@Query("IsApproved") boolean isApproved, @Query("ProjectNameorID") String projectNameorID);



    @GET("GetAllProjects")
    Call<ProjectsJsonResult> projectsAvilables();

    @GET("GetAllApprovedProjects")
    Call<ApprovedProjectsJsonResult> approvedpProjects();


}
