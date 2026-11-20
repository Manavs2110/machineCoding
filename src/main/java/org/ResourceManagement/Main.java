package org.ResourceManagement;

import java.util.*;

enum Role{
    Admin, Read, Write;
    public static boolean isValid(String role){
        for(Role r:values()){
           if(r.name().equals(role)) return true;
        }
        return false;
    }
}

class Employee{
    String employeeId;
    String employeeName;
    public Employee(String employeeId, String employeeName){
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }
}
class Resource{
    String resourceId;
    String resourceName;
    public Resource(String resourceId, String resourceName){
        this.resourceId = resourceId;
        this.resourceName = resourceName;
    }
}
class AccessRecord{
    String employeeId;
    String resourceId;
    Role role;

    public AccessRecord(String employeeId, String resourceId, Role role) {
        this.employeeId = employeeId;
        this.resourceId = resourceId;
        this.role = role;
    }
}
class AccessManagement{
    Map<String, Employee> employeeMap;
    Map<String, Resource> resourceMap;
    List<AccessRecord> accessRecords;
    public AccessManagement(){
        employeeMap = new HashMap<>();
        resourceMap = new HashMap<>();
        accessRecords = new LinkedList<>();
    }
    public void addEmployee(String employeeId, String employeeName){
        if(employeeMap.containsKey(employeeId)){
            System.out.println("Error: Employee ID " + employeeId + " already exists.");
            return;
        }
        employeeMap.put(employeeId, new Employee(employeeId, employeeName));
    }
    public void addResource(String resourceId, String resourceName){
        if(resourceMap.containsKey(resourceId)){
            System.out.println("Error: Resource ID " + resourceId + " already exists.");
            return;
        }
        resourceMap.put(resourceId, new Resource(resourceId, resourceName));
    }
    public void fetchResourceByEmployee(String employeeId){
        try {
            if (!employeeMap.containsKey(employeeId)) {
                throw new RuntimeException("Employee ID " + employeeId + " not found");
            }
            List<String> filteredRecords = accessRecords.stream().filter(accessRecord -> accessRecord.employeeId.equals(employeeId)).map(accessRecord -> accessRecord.resourceId).toList();
            System.out.println(filteredRecords);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }
    public void fetchEmployeesByResource(String resourceId){
        try{
            if(!resourceMap.containsKey(resourceId)){
                throw new RuntimeException("Resource ID "+resourceId+" not found");
            }
            List<String> filteredRecords = accessRecords.stream().filter(accessRecord -> accessRecord.resourceId.equals(resourceId)).map(accessRecord -> accessRecord.employeeId).toList();
            System.out.println(filteredRecords);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }


    }
    public void assignResource(String employeeId, String resourceId, String role){
        try{
            if(!employeeMap.containsKey(employeeId)) {
                throw new RuntimeException("Employee ID "+employeeId+" not found");
            }
            if(!resourceMap.containsKey(resourceId)){
                throw new RuntimeException("Resource ID "+resourceId+" not found");
            }
            if(!Role.isValid(role)){
                throw  new RuntimeException("Invalid access level: "+role);
            }
            for(AccessRecord accessRecord: accessRecords){
                if(accessRecord.resourceId.equals(resourceId) && accessRecord.employeeId.equals(employeeId)){
                    accessRecord.role = Role.valueOf(role);
                    return;
                }
            }
            accessRecords.add(new AccessRecord(employeeId, resourceId, Role.valueOf(role)));
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }
    public void removeAccess(String employeeId, String resourceId){
        try{
            if(!employeeMap.containsKey(employeeId)) {
                throw new RuntimeException("Employee ID "+employeeId+" not found");
            }
            if(!resourceMap.containsKey(resourceId)){
                throw new RuntimeException("Resource ID "+resourceId+" not found");
            }
            if(!accessRecords.removeIf(accessRecord -> (accessRecord.resourceId.equals(resourceId) && accessRecord.employeeId.equals(employeeId)))){
                throw new RuntimeException("No access record found for Employee "+employeeId+" on Resource " +resourceId);
            }
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }
}
public class Main {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        AccessManagement accessManagement = new AccessManagement();
        while(true){
            String line = sc.nextLine().trim();
            String[] parts = line.split(" ");
            switch(parts[0]){
                case "ADD_EMPLOYEE"-> accessManagement.addEmployee(parts[1], parts[2]);
                case "ADD_RESOURCE"-> accessManagement.addResource(parts[1], parts[2]);
                case "GRANT_ACCESS"-> accessManagement.assignResource(parts[1], parts[2], parts[3]);
                case "REVOKE_ACCESS"->accessManagement.removeAccess(parts[1],parts[2]);
                case "GET_RESOURCES_BY_EMPLOYEE"-> accessManagement.fetchResourceByEmployee(parts[1]);
                case "GET_EMPLOYEES_BY_RESOURCE"-> accessManagement.fetchEmployeesByResource(parts[1]);
                default -> System.out.println("INVALID INPUT");
            }
        }
    }
}
