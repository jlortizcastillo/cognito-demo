package pe.com.visanet.demos.cognito;

/**
 *
 * @author jorgeluis
 */
public class App {
    
    public static void main(String[] args)  {
        createGroup();
        createUser();
        editUser();
        deleteUser();
    }
    
    private static void createGroup(){
        String groupName = "";
        String groupDescription = "";
        
        if(CognitoManager.createGroup(groupName, groupDescription)){
            System.out.println("Group created!");
        }else{
            System.out.println("Group wasn't created!");
        }
    }
    
    private static void createUser(){
        String userName = "";
        String name = "";
        String lastName = "";
        String groupName = "";
        
        if(CognitoManager.createUser(userName, name, lastName)){
            System.out.println("User created!");
            
            if(!groupName.isEmpty()){
                if(CognitoManager.addUserToGroup(userName, groupName)) {
                    System.out.println("User added to group!");
                }
            }
        } else {
            System.out.println("User wasn't created!");
        }
    }
    
    private static void editUser(){
        String userName = "";
        String name = "";
        String lastName = "";
        
        if(CognitoManager.editUser(userName, name, lastName)){
            System.out.println("User updated!");
        } else {
            System.out.println("User wasn't updated!");
        }
    }
    
    private static void deleteUser(){
        String userName = "";
        
        if(CognitoManager.deleteUser(userName)){
            System.out.println("User updated!");
        } else {
            System.out.println("User wasn't updated!");
        }
    }
}
