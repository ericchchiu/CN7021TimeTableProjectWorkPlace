package currentWorkingDen171205;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by Eric's laptop on 16/11/2017.
 */
public class CheckRoomSizeSufficiencyImpl implements CheckRoomSizeSufficiency {

    @Override
    public void checkRoomSizeSufficiency(ArrayList<ModuleImpl> moduleCodes){

        ArrayList<ArrayList<String>> roomSizeList = new ArrayList<>();
        try {
            Files.lines(Paths.get("one711131409RoomSizeSortedCurrentWorking.txt")).
                    forEach(s ->
                        {String[] ss=s.split(",");
                         roomSizeList.add(new ArrayList<String>(Arrays.asList(ss)));});
        } catch (IOException e) {
            System.out.println(e);
        }
        //1711161604 temp code
        //for(ArrayList<String> as:roomSizeList){
        //    System.out.println(as);
        //}
        //1711161604 temp code end //deleted 1607
        System.out.println();
        System.out.println("Adequacy of rooms allocated to lectures, tutorials and practicals: ");
        for (ModuleImpl m:moduleCodes){
            System.out.println(m.getCourseCode()+":");
            System.out.println("  Number of students: "+m.getStudentNum());
            int count = 0;
            for (ArrayList<String> as : m.getTypeOfMeetingNRmNumList()) {
                if ((as.get(0).trim().toLowerCase().contains("tutorial") ||
                        as.get(0).trim().toLowerCase().contains("practical"))&&
                        !as.get(0).trim().toLowerCase().contains("lecture")) {
                    count++;
                }
                if (as.get(0).trim().toLowerCase().contains("lecture")){
                    /*int roomSize=0;
                    boolean rmFound=false;*/
                    ArrayList<String> asList = new ArrayList<>(Arrays.asList(as.get(1).split("/")));
                    Iterator<String> itr=asList.iterator();
                    while(itr.hasNext()) {
                        boolean rmFound=false;
                        String itr1 =itr.next();
                    for(ArrayList<String> rs:roomSizeList) {
                        int roomSize=0;
                        //boolean rmFound=false;
                        /*for (String[] strings: roomList){
                            Iterator<String> itr=dayRoom.iterator();
                            while(itr.hasNext()) {
                                //System.out.println(itr.next());  //temp

                                if(itr.next().contains(strings[0])){
                                    System.out.printf("Room %s size is: %s\n", strings[0].trim(),strings[1].trim());
                                }
                            }*/

                        if (itr1.trim().replace(".","").toLowerCase().
                                contains(rs.get(0).trim().replace(".","").toLowerCase())) {

                            roomSize = Integer.parseInt(rs.get(1).trim());
                            System.out.printf("  The room %s assigned to this module for %s can acommodate %d students.\n",
                                    itr1.trim(), as.get(0).toLowerCase(), roomSize);
                            if (m.getStudentNum() <= roomSize)
                                System.out.println("  The room can accommodate the " + m.getStudentNum() + " students");
                            else
                                System.out.println("  The room is too small for the " + m.getStudentNum() + " students");
                            rmFound = true;
                        }
                    }
                    if(rmFound==false) System.out.printf("  The number of the room (%s) assigned to this module for %s does not" +
                            " in the room list or no room has been assigned.\n", itr1.trim(), as.get(0).trim());
                }}
            }
            if (count == 0) {
                System.out.println("  This module does not has tutorial/practical or " +
                        "lectures and tutorials/practicals are combined.");
            }
            else if (m.getStudentNum() <= 25 * count && m.getStudentNum() > 25 * (count - 1)) {
                System.out.printf("  Number of tutorial/Practical group(s) (%d) set is adequate.", count);
                System.out.println();
                System.out.println("  The room(s) allocated to the tutorials/practicals are/is:");
                //int totalNumRoomsCanAccomm = 0;
                ArrayList<ArrayList<Integer>> studentNumArray = new ArrayList<>();
                //boolean foundRm = false;
                abcde:
                for (int i = 0; i < m.getTypeOfMeetingNRmNumList().size(); i++) {
                    //for (ArrayList<String> as : m.getTypeOfMeetingNRmNumList()) {
                    studentNumArray.add(new ArrayList<Integer>());
                    if (!m.getTypeOfMeetingNRmNumList().get(i).get(0).trim().toLowerCase().contains("lecture") &&
                            (m.getTypeOfMeetingNRmNumList().get(i).get(0).trim().toLowerCase().contains("tutorial") ||
                                    m.getTypeOfMeetingNRmNumList().get(i).get(0).trim().toLowerCase().contains("practical"))) {
                        System.out.println("  Group " + i + ":");
                        boolean foundRm = false;
                        ArrayList<String> tutOrPraRmsPerGroup = new ArrayList<String>
                                (Arrays.asList(m.getTypeOfMeetingNRmNumList().get(i).get(1).split("/")));
                        Iterator<String> itr = tutOrPraRmsPerGroup.iterator();
                        while (itr.hasNext()) {
                            String itr1 = itr.next();
                            for (ArrayList<String> rs : roomSizeList) {
                                if (itr1.replace(".","").toLowerCase().
                                        equals(rs.get(0).replace(".","").toLowerCase())) {
                                    System.out.printf(
                                            "  Room %s: Size: %s person. Therefore, maximum tutorial " +
                                                    "group size in this room: %d",
                                            rs.get(0), rs.get(1), Math.min(25, Integer.parseInt(rs.get(1))));
                                    System.out.println();
                                    studentNumArray.get(i).add(Math.min(25, Integer.parseInt(rs.get(1))));
                                    foundRm = true;
                                }
                                /*if (foundRm == false) {
                                    System.out.printf("  Room number %s is not in the room list, analysis for this module" +
                                            " cannot proceed further.\n", itr1);
                                    break abcde;
                                }
                                foundRm = false;*/
                            }
                            if (foundRm == false) {
                                System.out.printf("  Room number %s is not in the room list, analysis for this module" +
                                        " cannot proceed further.\n", itr1);
                                break abcde;
                            }
                            foundRm = false;
                        }
                        boolean groupAssignedMoreThanOneRoom = false;
                        if (!m.getTypeOfMeetingNRmNumList().get(i).get(0).trim().toLowerCase().contains("lecture") &&
                                i == m.getTypeOfMeetingNRmNumList().size() - 1) {
                            int roomMinTotStudentNum = 0;
                            for (ArrayList<Integer> studentNumArray1 : studentNumArray) {
                                if (studentNumArray1.size() != 0) {
                                    roomMinTotStudentNum = roomMinTotStudentNum+Collections.min(studentNumArray1);
                                }
                                if (studentNumArray1.size() > 1) groupAssignedMoreThanOneRoom = true;
                            }
                            if (m.getStudentNum() > roomMinTotStudentNum) {
                                //temp, below line for checking 1711250934
                                System.out.println(m.getStudentNum()+" "+roomMinTotStudentNum);
                                if (count > 1 && groupAssignedMoreThanOneRoom == true) {
                                    System.out.printf("  Note: As there are more than one group and there are more than " +
                                            "one room assigned to a group and it is in existence of the sum " +
                                            "of permitted student numbers calculated from selecting one room each" +
                                            " from the groups which is less than the total number of students of the module" +
                                            " (%d), please check carefully and rectify if necessary.\n", roomMinTotStudentNum);
                                } else
                                    System.out.printf("  There are/is tutorial/practical group(s) which can only " +
                                            "accommodate %d students. Larger room(s) should be found to replace the " +
                                            "smaller one(s).\n", roomMinTotStudentNum);
                            } else System.out.println("  The size of the room(s) allocated to the " +
                                    "tutorial/practical group(s) is also sufficient.");
                        }
                    }

                }
            }
            //else System.out.printf("  Tutorial/Practical groups set (%s) are either too many or not sufficient. " +
              //          "please use Task 3 to find out the details.\n", count);
            else if (m.getStudentNum() > 25 * count) {
                int additionalGroupNum = (int) Math.ceil(m.getStudentNum() / 25d) - count;
                System.out.printf("  As there are %d students and there are/is only %d tutorial/practical group(s), " +
                                "%d tutorial/practical group(s) need to be added.\n" +
                                "  (Caveat: classroom size should be taken into consideration after adding " +
                                "tutorial/practical group(s))",
                        m.getStudentNum(), count, additionalGroupNum);
                System.out.println();
            }
            else {
                int reducingGroupNum = count - (int) Math.ceil(m.getStudentNum() / 25d);
                System.out.printf("  As there are %d students and there are/is %d tutorial/practical group(s), " +
                                "%d tutorial/practical group(s) need to be withdrawn.\n" +
                                "  (Caveat: classroom size should be taken into consideration after deduction " +
                                "of tutorial/practical group(s))",
                        m.getStudentNum(), count, reducingGroupNum);
                System.out.println();
            }
        }
    }
}
