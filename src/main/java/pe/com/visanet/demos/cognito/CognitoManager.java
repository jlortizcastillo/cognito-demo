
package pe.com.visanet.demos.cognito;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.model.AdminAddUserToGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminEnableUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AdminListGroupsForUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminListGroupsForUserResult;
import com.amazonaws.services.cognitoidp.model.AdminRemoveUserFromGroupRequest;
import com.amazonaws.services.cognitoidp.model.AdminResetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.CreateGroupRequest;
import com.amazonaws.services.cognitoidp.model.DeleteGroupRequest;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.amazonaws.services.cognitoidp.model.GroupType;
import com.amazonaws.services.cognitoidp.model.ListGroupsRequest;
import com.amazonaws.services.cognitoidp.model.ListGroupsResult;
import com.amazonaws.services.cognitoidp.model.UpdateGroupRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jorgeluis
 */
public class CognitoManager {
    private static final String ACCESS_KEY_ID = "<<your access key id>>";
    private static final String SECRET_KEY_ID = "<<your secret key id>>";
    private static final String POOL_ID = "<<your pool id>>";
    private static final String CLIENT_ID = "<<your client id>>";
    protected static final Region REGION_POOL = Region.getRegion(Regions.US_EAST_1);
    
    protected static final BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY_ID);
    protected static AWSCognitoIdentityProviderClient awsCognitoClient = new AWSCognitoIdentityProviderClient(awsCredentials);
    
    public CognitoManager(){
        
    }
    
    //User operations
    
    public static boolean createUser(String userName, String firstName, String lastName) {        
        try {
            AdminCreateUserRequest cognitoRequest = new AdminCreateUserRequest()
                    .withUserPoolId(POOL_ID)
                    .withUsername(userName.toLowerCase())
                    .withUserAttributes(
                            new AttributeType()
                                    .withName("email")
                                    .withValue(userName.toLowerCase()),
                            new AttributeType()
                                    .withName("email_verified")
                                    .withValue("true"),
                            new AttributeType()
                                    .withName("name")
                                    .withValue(firstName),
                            new AttributeType()
                                    .withName("family_name")
                                    .withValue(lastName)
                    )
                    .withDesiredDeliveryMediums(DeliveryMediumType.EMAIL)
                    .withForceAliasCreation(Boolean.FALSE);
                    
            awsCognitoClient.adminCreateUser(cognitoRequest);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean editUser(String userName, String firstName, String lastName) {
                
        try {
            AdminUpdateUserAttributesRequest cognitoRequest = new AdminUpdateUserAttributesRequest()
                    .withUserPoolId(POOL_ID)
                    .withUsername(userName.toLowerCase())
                    .withUserAttributes(
                            new AttributeType()
                                    .withName("email")
                                    .withValue(userName),
                            new AttributeType()
                                    .withName("name")
                                    .withValue(firstName),
                            new AttributeType()
                                    .withName("family_name")
                                    .withValue(lastName)
                    );

            awsCognitoClient.adminUpdateUserAttributes(cognitoRequest);
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean deleteUser(String userName){
        try {
            AdminDeleteUserRequest request = new AdminDeleteUserRequest()
                    .withUserPoolId(POOL_ID)
                    .withUsername(userName);
            
            awsCognitoClient.adminDeleteUser(request);
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static GetUserResult getUser(String accessToken) {
        try {
            GetUserRequest authRequest = new GetUserRequest().withAccessToken(accessToken);
        
            return awsCognitoClient.getUser(authRequest);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
        
    private static AdminInitiateAuthResult authenticate(String userName, String password) {
        try {
            Map<String, String> authParams = new HashMap<>();
            authParams.put("USERNAME", userName);
            authParams.put("PASSWORD", password);

            AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .withAuthParameters(authParams)
                    .withClientId(CLIENT_ID)
                    .withUserPoolId(POOL_ID);

            return awsCognitoClient.adminInitiateAuth(authRequest);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
        
    public static boolean resetCognitoPassword(String userName) {        
        try {
            AdminResetUserPasswordRequest cognitoRequest = new AdminResetUserPasswordRequest()
                    .withUserPoolId(POOL_ID)
                    .withUsername(userName);

            awsCognitoClient.adminResetUserPassword(cognitoRequest);
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean changePassword(String userName, String oldPassword, String newPassword){
        try {
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean recoverPassword(String userName, String newPassword, String verificationCode){
        try {
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean enableUser(String userName){
        try {
            AdminEnableUserRequest request = new AdminEnableUserRequest()
                                                        .withUserPoolId(POOL_ID)
                                                        .withUsername(userName);
            
            awsCognitoClient.adminEnableUser(request);
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean disableUser(String userName){
        try {
            AdminDisableUserRequest request = new AdminDisableUserRequest()
                                                        .withUserPoolId(POOL_ID)
                                                        .withUsername(userName);
            
            awsCognitoClient.adminDisableUser(request);
                        
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    //Groups Operations
    
    public static boolean createGroup(String groupName, String description){
        try {
            
            CreateGroupRequest request = new CreateGroupRequest()
                                                .withUserPoolId(POOL_ID)
                                                .withGroupName(groupName)
                                                .withDescription(description);
            
            awsCognitoClient.createGroup(request);
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean updateGroup(String groupName, String description){
        try {
            
            UpdateGroupRequest request = new UpdateGroupRequest()
                                                .withUserPoolId(POOL_ID)
                                                .withGroupName(groupName)
                                                .withDescription(description);
            
            awsCognitoClient.updateGroup(request);
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean deleteGroup(String groupName){
        try {
            DeleteGroupRequest request = new DeleteGroupRequest()
                                                .withUserPoolId(POOL_ID)
                                                .withGroupName(groupName);
            
            awsCognitoClient.deleteGroup(request);
            
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static List<String> getListGroups(){
        try {
            List<String> groups = new ArrayList<>();
            
            ListGroupsRequest request = new ListGroupsRequest()
                                        .withUserPoolId(POOL_ID)
                                        .withLimit(60);
            
            ListGroupsResult groupsResult = awsCognitoClient.listGroups(request);
            
            if (groupsResult != null) {
                for (GroupType g : groupsResult.getGroups()) {
                    groups.add(g.getGroupName());
                }
            }
            
            return groups;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            
            return new ArrayList<>();
        }
    }
    
    public static boolean addUserToGroup(String userName, String groupName) {
        try {
            AdminAddUserToGroupRequest cognitoRequest = new AdminAddUserToGroupRequest()
                    .withUserPoolId(POOL_ID)
                    .withUsername(userName)
                    .withGroupName(groupName);

            awsCognitoClient.adminAddUserToGroup(cognitoRequest);
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static boolean removeUserFromGroup(String userName, String groupName){
        try {
            AdminRemoveUserFromGroupRequest request = new AdminRemoveUserFromGroupRequest()
                                                            .withUserPoolId(POOL_ID)
                                                            .withGroupName(groupName)
                                                            .withUsername(userName);
            
            awsCognitoClient.adminRemoveUserFromGroup(request);
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    public static List<String> getListGroupsForUser(String userName) {
        
        try {
            AdminListGroupsForUserRequest groupRequest = new AdminListGroupsForUserRequest()
                                                                .withUsername(userName)
                                                                .withUserPoolId(POOL_ID)
                                                                .withLimit(60);
            
            AdminListGroupsForUserResult groupsResult = awsCognitoClient.adminListGroupsForUser(groupRequest);
            List<String> groups = new ArrayList<>();
            
            if (groupsResult != null) {
                for (GroupType g : groupsResult.getGroups()) {
                    groups.add(g.getGroupName());
                }
            }
            return groups;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }
}
