package org.ResourceManagement;

import java.util.*;
class AccessManagementRefined{
    Map<String, Employee> employeeMap;
    Map<String, Resource> resourceMap;
    Map<String, Map<String, AccessRecord>> byEmployee;
    Map<String, Map<String, AccessRecord>> byResource;

    public AccessManagementRefined(){
        employeeMap = new HashMap<>();
        resourceMap = new HashMap<>();
    }
    public void addEmployee(String employeeId, String employeeName){
        try{
            if(employeeMap.containsKey(employeeId)){
                throw new RuntimeException("Employee "+employeeId+" already exist");
            }
            employeeMap.put(employeeId, new Employee(employeeId,employeeName));
            byEmployee.put(employeeId, new LinkedHashMap<>());
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void addResource(String resourceId, String resourceName){
        try{
            if(resourceMap.containsKey(resourceId)){
                throw new RuntimeException("Resource "+resourceId+" already exist");
            }
            resourceMap.put(resourceId, new Resource(resourceId, resourceName));
            byResource.put(resourceId, new LinkedHashMap<>());
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void assignResource(String employeeId, String resourceId, String role){
        try {
            if (!employeeMap.containsKey(employeeId)) {
                throw new RuntimeException("Employee ID " + employeeId + " not found");
            }
            if (!resourceMap.containsKey(resourceId)) {
                throw new RuntimeException("Resource ID " + resourceId + " not found");
            }
            if (!Role.isValid(role)) {
                throw new RuntimeException("Invalid access level: " + role);
            }
            AccessRecord accessRecord = new AccessRecord(employeeId, resourceId, Role.valueOf(role));
            byEmployee.get(employeeId).put(resourceId, accessRecord);
            byResource.get(resourceId).put(employeeId, accessRecord);
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void revokeAccess(String employeeId, String resourceId){
        try{
            if(!employeeMap.containsKey(employeeId)) {
                throw new RuntimeException("Employee ID "+employeeId+" not found");
            }
            if(!resourceMap.containsKey(resourceId)){
                throw new RuntimeException("Resource ID "+resourceId+" not found");
            }
            if(!byEmployee.get(employeeId).containsKey(resourceId)){
                throw new RuntimeException("No access record found for Employee "+employeeId+" on Resource " +resourceId);
            }
            byEmployee.get(employeeId).remove(resourceId);
            byResource.get(resourceId).remove(employeeId);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }
    public void getResourceByEmployee(String employeeId){
        try {
            if (!employeeMap.containsKey(employeeId)) {
                throw new RuntimeException("Employee ID " + employeeId + " not found");
            }
            List<String> filteredRecords = byEmployee.get(employeeId).values().stream().map(accessRecord -> accessRecord.resourceId).toList();
            System.out.println(filteredRecords);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }
    public void getEmployeeByResource(String resourceId){
        try{
            if(!resourceMap.containsKey(resourceId)){
                throw new RuntimeException("Resource ID "+resourceId+" not found");
            }
            List<String> filteredRecords = byResource.get(resourceId).values().stream().map(accessRecord -> accessRecord.employeeId).toList();
            System.out.println(filteredRecords);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }
}
public class Main2 {
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
