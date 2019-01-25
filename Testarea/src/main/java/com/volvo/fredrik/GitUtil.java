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
package com.volvo.fredrik;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.CommitTimeRevFilter;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

/**
 * Utility class for Dates.
 */
// CHECKSTYLE:OFF: checkstyle:magicnumber
public class GitUtil {

    public GitUtil() {
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
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

    public static void main(String args[]) {
        String log = getGitLog("remotes/origin/master", 2, 30);

        System.out.println(log);
    }

    private static String getGitLog(String branch, int daysBack, int maxCount) {
        StringBuffer buf = new StringBuffer();

        // saveId("b0d1203");

        String prevId = getPreviousId();
        GitUtil gitUtil = new GitUtil();

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = null;
        Git git = null;
        try {
            repository = repositoryBuilder.readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .setMustExist(true).build();
            git = new Git(repository);

            // Setting up log filter
            Date ts = daysBack(daysBack);
            RevFilter revFilter = CommitTimeRevFilter.after(ts);
            String untilRev = prevId;
            
            buf.append("Git history for: " + repository.getConfig().getString("remote", "origin", "url") + ", branch: " + branch + ", since: "
                    + dateToString(ts) + ", until revision: " + prevId + ", max: " + maxCount);
            buf.append("\n");

            // Retrieving git log
            Iterable<RevCommit> logs = git.log()
                    .add(repository.resolve(branch))
                    .setMaxCount(maxCount)
                    .setRevFilter(revFilter).call();


            String firstRev = null;
            for (RevCommit rev : logs) {
                if (firstRev == null) {
                    firstRev = rev.getName();
                }
                if (untilRev != null && rev.getName().startsWith(untilRev)) {
                    break;
                }

                buf.append("" + rev.getFullMessage().trim());
                buf.append(" Name: " + rev.getCommitterIdent().getName());
                buf.append(" Time: " + rev.getCommitterIdent().getWhen());
                buf.append(" Commit: " + rev.getName());
                buf.append("\n");
            }

            saveId(firstRev);
            
        } catch (NoHeadException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (RevisionSyntaxException e) {
            e.printStackTrace();
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (AmbiguousObjectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return buf.toString();
    }

    private static String getPreviousId() {
        String toret = null;
        try {
            toret = FileUtils.readFileToString(new File("commit_id_state.txt"));
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

}
