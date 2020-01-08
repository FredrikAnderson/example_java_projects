/*
 * Copyright 2009 Volvo Information Technology AB 
 * 
 * Licensed under the Volvo IT Corporate Source License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://java.volvo.com/licenses/CORPORATE-SOURCE-LICENSE 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.volvo.jenkins.application.infra.cmd;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;

/**
 * Utility class for Dates.
 */
public class GitUtil {
   
    private GitUtil() {}
    
    public static void main(String args[]) {
        String log = getGitLog("remotes/origin/master", 2, 30);

        System.out.println(log);
    }

    public static String getGitLog(String branch, int daysBack, int maxCount) {
        StringBuilder buf = new StringBuilder();

//        String prevId = getPreviousId();
//
//        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
//        Repository repository = null;
//        Git git = null;
//        try {
//            repository = repositoryBuilder.readEnvironment() // scan environment GIT_* variables
//                    .findGitDir() // scan up the file system tree
//                    .setMustExist(true).build();
//            git = new Git(repository);
//
//            // Setting up log filter
//            Date ts = daysBack(daysBack);
//            RevFilter revFilter = CommitTimeRevFilter.after(ts);
//            String untilRev = prevId;
//            
//            buf.append("Git history for: " + repository.getConfig().getString("remote", "origin", "url") 
//                       + ", branch: " + branch + ", from: " + daysBack + " days at: "
//                    + dateToString(ts) + ", until revision: " + prevId + ", max: " + maxCount);
//            buf.append("<br><br>");
//
//            // Retrieving git log
//            Iterable<RevCommit> logs = git.log()
//                    .add(repository.resolve(branch))
//                    .setMaxCount(maxCount)
//                    .setRevFilter(revFilter).call();
//
//
//            String firstRev = null;
//            buf.append("<table>");
//            buf.append("<tr>");
//            buf.append("<td><b>Message</b></td><td><b>Name</b></td><td><b>Time</b></td><td><b>Commit</b></td>");            
//            buf.append("</tr>");
//            for (RevCommit rev : logs) {
//                buf.append("<tr>");
//
//                if (firstRev == null) {
//                    firstRev = rev.getName();
//                }
//                if (untilRev != null && rev.getName().startsWith(untilRev)) {
//                    break;
//                }
//
//                buf.append("<td>" + rev.getFullMessage().trim() + "</td>");
//                buf.append("<td>" + rev.getCommitterIdent().getName() + "</td>");
//                buf.append("<td>" + dateToString(rev.getCommitterIdent().getWhen()) + "</td>");
//                buf.append("<td>" + rev.getName().substring(0, 8) + "</td>");
//
//                buf.append("</tr>");
//            }
//            buf.append("</table>");
//
//            saveId(firstRev);
//            
//        } catch (NoHeadException e) {
//            e.printStackTrace();
//        } catch (GitAPIException e) {
//            e.printStackTrace();
//        } catch (RevisionSyntaxException e) {
//            e.printStackTrace();
//        } catch (MissingObjectException e) {
//            e.printStackTrace();
//        } catch (IncorrectObjectTypeException e) {
//            e.printStackTrace();
//        } catch (AmbiguousObjectException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        return buf.toString();
    }

    
    private static String getPreviousId() {
        String toret = null;
        try {
            toret = FileUtils.readFileToString(new File("commit_id_state.txt"));
            System.out.println("Found a previous commit id: " + toret);
        } catch (IOException e) {
            // File / state not found
        }
        return toret;
    }

    private static void saveId(String commitId) {
        try {
            FileUtils.writeStringToFile(new File("commit_id_state.txt"), commitId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Date daysBack(int daysBack) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysBack);
        return cal.getTime();
    }

    private static String dateToString(Date dateToStr) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(dateToStr);
    }

}
