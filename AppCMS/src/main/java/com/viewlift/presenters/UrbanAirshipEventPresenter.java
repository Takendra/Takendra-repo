package com.viewlift.presenters;

import com.urbanairship.UAirship;
import com.viewlift.models.data.urbanairship.UAAudience;
import com.viewlift.models.data.urbanairship.UANamedUserRequest;

import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.functions.Action1;

//import com.viewlift.models.data.appcms.sites.Notifications;

/**
 * Created by viewlift on 12/18/17.
 */

public class UrbanAirshipEventPresenter {
    private static final String SUBSCRIPTION_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    private static final String YYMMDD_DATE_FORMAT = "yyyy-MM-dd";
    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC+00:00");

    private String loggedInStatusGroup;
    private String loggedInStatusTag;
    private String loggedOutStatusTag;
    private String subscriptionStatusGroup;
    private String subscribedTag;
    private String subscriptionAboutToExpireTag;
    private String unsubscribedTag;
    private String subscriptionEndDateGroup;
    private String subscriptionPlanGroup;
    private int daysBeforeSubscriptionEndForNotification;

    private String lapsed;
    private String lapsedTagGroup;
    private String subscribedTagMOTV;
    private String freeTrialNoStartTagGroup;
    private String freeTrialNoStartTag;
    private String unengagedTag;
    private String videoCategoryTagGroup;

    @Inject
    public UrbanAirshipEventPresenter(String loggedInStatusGroup,
                                      String loggedInStatusTag,
                                      String loggedOutStatusTag,
                                      String subscriptionStatusGroup,
                                      String subscribedTag,
                                      String subscriptionAboutToExpireTag,
                                      String unsubscribedTag,
                                      String subscriptionEndDateGroup,
                                      String subscriptionPlanGroup,
                                      int daysBeforeSubscriptionEndForNotification,
                                      String lapsed,
                                      String lapsedTagGroup,
                                      String subscribedTagMOTV,
                                      String freeTrialNoStartTagGroup,
                                      String freeTrialNoStartTag,
                                      String unengagedTag,
                                      String videoCategoryTagGroup) {

        this.loggedInStatusGroup = loggedInStatusGroup;
        this.loggedInStatusTag = loggedInStatusTag;
        this.loggedOutStatusTag = loggedOutStatusTag;
        this.subscriptionStatusGroup = subscriptionStatusGroup;
        this.subscribedTag = subscribedTag;
        this.subscriptionAboutToExpireTag = subscriptionAboutToExpireTag;
        this.unsubscribedTag = unsubscribedTag;
        this.subscriptionEndDateGroup = subscriptionEndDateGroup;
        this.subscriptionPlanGroup = subscriptionPlanGroup;
        this.daysBeforeSubscriptionEndForNotification = daysBeforeSubscriptionEndForNotification;
        this.lapsed = lapsed;
        this.lapsedTagGroup = lapsedTagGroup;
        this.subscribedTagMOTV = subscribedTagMOTV;
        this.freeTrialNoStartTagGroup = freeTrialNoStartTagGroup;
        this.freeTrialNoStartTag = freeTrialNoStartTag;
        this.unengagedTag = unengagedTag;
        this.videoCategoryTagGroup = videoCategoryTagGroup;
    }

    public void sendUserLoginEvent(String userId,
                                   Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();


        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(loggedInStatusTag);
        uaAdd.put(loggedInStatusGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        Map<String, List<String>> uaRemove = new HashMap<>();
        List<String> uaRemoveList = new ArrayList<>();
        uaRemoveList.add(loggedOutStatusTag);
        uaRemove.put(loggedInStatusGroup, uaRemoveList);
        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }

        // NOTE: The following SDK implementation does not update the tags for the give user
        // Testing executed using the following UA endpoint:
        // https://go.urbanairship.com/api/named_users/?id=<userId>
        // Http Headers:
        // Authorization: Basic <Base64 Encoded username:password> (See UANamedUserEventCall.getBasicAuthHeaderValue())
        // Accept: application/vnd.urbanairship+json; version=3;
        // Content-Type: application/json
        UAirship.shared().getNamedUser().setId(userId);
        UAirship.shared()
                .getNamedUser()
                .editTagGroups()
                .addTag(loggedInStatusGroup, loggedInStatusTag)
                .removeTag(loggedInStatusGroup, loggedOutStatusTag)
                .apply();
        UAirship.shared()
                .getNamedUser()
                .forceUpdate();
    }

    public void sendUserLogoutEvent(String userId,
                                    Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();


        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(loggedOutStatusTag);
        uaAdd.put(loggedInStatusGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        Map<String, List<String>> uaRemove = new HashMap<>();
        List<String> uaRemoveList = new ArrayList<>();
        uaRemoveList.add(loggedInStatusTag);
        uaRemove.put(loggedInStatusGroup, uaRemoveList);
        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }

        // NOTE: The following SDK implementation does not update the tags for the give user
        // Testing executed using the following UA endpoint:
        // https://go.urbanairship.com/api/named_users/?id=<userId>
        // Http Headers:
        // Authorization: Basic <Base64 Encoded username:password> (See UANamedUserEventCall.getBasicAuthHeaderValue())
        // Accept: application/vnd.urbanairship+json; version=3;
        // Content-Type: application/json
        UAirship.shared().getNamedUser().setId(userId);
        UAirship.shared()
                .getNamedUser()
                .editTagGroups()
                .addTag(loggedInStatusGroup, loggedOutStatusTag)
                .removeTag(loggedInStatusGroup, loggedInStatusTag)
                .apply();
        UAirship.shared()
                .getNamedUser()
                .forceUpdate();
    }

    public void sendSubscribedEvent(String userId,
                                    Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();


        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(subscribedTag);
        uaAdd.put(subscriptionStatusGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        Map<String, List<String>> uaRemove = new HashMap<>();
        List<String> uaRemoveList = new ArrayList<>();
        uaRemoveList.add(unsubscribedTag);
        uaRemoveList.add(subscriptionAboutToExpireTag);
        uaRemove.put(subscriptionStatusGroup, uaRemoveList);
        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }
    }

    public void sendSubscriptionAboutToExpireEvent(String userId,
                                                   Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();

        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(subscriptionAboutToExpireTag);
        uaAdd.put(subscriptionStatusGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        Map<String, List<String>> uaRemove = new HashMap<>();
        List<String> uaRemoveList = new ArrayList<>();
        uaRemoveList.add(subscribedTag);
        uaRemoveList.add(unsubscribedTag);
        uaRemove.put(subscriptionStatusGroup, uaRemoveList);
        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }
    }

    public void sendUnsubscribedEvent(String userId,
                                      Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();

        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(unsubscribedTag);
        uaAdd.put(subscriptionStatusGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        Map<String, List<String>> uaRemove = new HashMap<>();
        List<String> uaRemoveList = new ArrayList<>();
        uaRemoveList.add(subscribedTag);
        uaRemoveList.add(subscriptionAboutToExpireTag);
        uaRemove.put(subscriptionStatusGroup, uaRemoveList);
        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }
    }

    public void sendSubscriptionEndDateEvent(String userId,
                                             String subscriptionEndDate,
                                             Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();

        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(getZonedDateTimeYYMMDD(subscriptionEndDate));
        uaAdd.put(subscriptionEndDateGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }
    }

    public void sendSubscriptionPlanEvent(String userId,
                                          String subscriptionPlan,
                                          Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();

        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(subscriptionPlan);
        uaAdd.put(subscriptionPlanGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);
        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }
    }

    public String getZonedDateTimeYYMMDD(String date) {
        ZonedDateTime zonedDateTime = ZonedDateTime.from(DateTimeFormatter.ofPattern(SUBSCRIPTION_DATE_FORMAT).parse(date));

        String zonedDateFormatYYMMDD = null;
        try {
            zonedDateFormatYYMMDD = zonedDateTime.format(DateTimeFormatter.ofPattern(YYMMDD_DATE_FORMAT));
        } catch (Exception e) {

        }

        return zonedDateFormatYYMMDD;
    }

    public boolean subscriptionAboutToExpire(String subscriptionEndDate) {
        if (!subscriptionExpired(subscriptionEndDate)) {
            ZonedDateTime nowTime = ZonedDateTime.now(UTC_ZONE_ID);
            ZonedDateTime subscriptionEndTime = ZonedDateTime.from(DateTimeFormatter.ofPattern(SUBSCRIPTION_DATE_FORMAT).parse(subscriptionEndDate));
            Period daysBeforeSubscriptionEnd = Period.ofDays(daysBeforeSubscriptionEndForNotification);
            return subscriptionEndTime.minus(daysBeforeSubscriptionEnd).toEpochSecond() <= nowTime.toEpochSecond();
        }

        return false;
    }

    public boolean subscriptionExpired(String subscriptionEndDate) {
        ZonedDateTime nowTime = ZonedDateTime.now(UTC_ZONE_ID);
        ZonedDateTime subscriptionEndTime = ZonedDateTime.from(DateTimeFormatter.ofPattern(SUBSCRIPTION_DATE_FORMAT).parse(subscriptionEndDate));

        return subscriptionEndTime.toEpochSecond() < nowTime.toEpochSecond();
    }


    /**
     * 11/06/2019
     *
     * @param userId        String type Loggedin user id
     * @param videoCategory String type category of the video.
     * @param sendAction    sending tagging data to UA.
     */
    public void videoCategoryUATagging(String userId, String videoCategory,
                                       Action1<UANamedUserRequest> sendAction) {

        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();
        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);


        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(videoCategory);
        uaAdd.put(videoCategoryTagGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }

        UAirship.shared().getNamedUser().setId(userId);
        UAirship.shared()
                .getNamedUser()
                .editTagGroups()
                .addTag(videoCategoryTagGroup, videoCategory)
//                .removeTag(subscriptionStatusGroup, unsubscribedTag)
                .apply();
        UAirship.shared()
                .getNamedUser()
                .forceUpdate();
    }

    /**
     * @param userId     String type
     * @param sendAction Api action call
     */
    public void sendUAAboutLapsedUser(String userId,
                                      Action1<UANamedUserRequest> sendAction) {

        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();

        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);


        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(loggedInStatusTag);
        uaAdd.put(lapsedTagGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        Map<String, List<String>> uaRemove = new HashMap<>();
        List<String> uaRemoveList = new ArrayList<>();
        uaRemoveList.add(unsubscribedTag);
        uaRemoveList.add(loggedInStatusTag);
        uaRemove.put(subscriptionStatusGroup, uaRemoveList);
        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {
        }

        UAirship.shared().getNamedUser().setId(userId);
        UAirship.shared()
                .getNamedUser()
                .editTagGroups()
                .addTag(lapsedTagGroup, loggedInStatusTag)
                .removeTag(subscriptionStatusGroup, unsubscribedTag)
                .apply();
        UAirship.shared()
                .getNamedUser()
                .forceUpdate();
    }


    public void sendMOTVSubscription(String userId,
                                     Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();

        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(subscribedTagMOTV);
        uaAdd.put(subscriptionStatusGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

        Map<String, List<String>> uaRemove = new HashMap<>();
        List<String> uaRemoveList = new ArrayList<>();
        uaRemoveList.add(unsubscribedTag);
        uaRemoveList.add(subscriptionAboutToExpireTag);
        uaRemove.put(subscriptionStatusGroup, uaRemoveList);
        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }

        UAirship.shared().getNamedUser().setId(userId);
        UAirship.shared()
                .getNamedUser()
                .editTagGroups()
                .addTag(subscriptionStatusGroup, subscribedTagMOTV)
                .removeTag(subscriptionStatusGroup, unsubscribedTag)
                .apply();
        UAirship.shared()
                .getNamedUser()
                .forceUpdate();

    }


    public void sendFreeTrialNoStartEvent(String userId,
                                          Action1<UANamedUserRequest> sendAction) {
        UANamedUserRequest uaNamedUserRequest = new UANamedUserRequest();


        UAAudience uaAudience = new UAAudience();
        uaAudience.addNamedUserIds(userId);
        uaNamedUserRequest.setUaAudience(uaAudience);

        Map<String, List<String>> uaAdd = new HashMap<>();
        List<String> uaAddList = new ArrayList<>();
        uaAddList.add(freeTrialNoStartTag);
        uaAdd.put(freeTrialNoStartTagGroup, uaAddList);
        uaNamedUserRequest.setUaAdd(uaAdd);

//        Map<String, List<String>> uaRemove = new HashMap<>();
//        List<String> uaRemoveList = new ArrayList<>();
//        uaRemoveList.add(loggedOutStatusTag);
//        uaRemove.put(freeTrialNoStartTagGroup, uaRemoveList);
//        uaNamedUserRequest.setUaRemove(uaRemove);

        try {
            sendAction.call(uaNamedUserRequest);
        } catch (Exception e) {

        }
        UAirship.shared().getNamedUser().setId(userId);
        UAirship.shared()
                .getNamedUser()
                .editTagGroups()
                .addTag(freeTrialNoStartTagGroup, freeTrialNoStartTag)
//                .removeTag(freeTrialNoStartTagGroup, loggedOutStatusTag)
                .apply();
        UAirship.shared()
                .getNamedUser()
                .forceUpdate();
    }
}
