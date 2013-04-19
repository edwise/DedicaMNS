/**
 * 
 */
package com.edwise.dedicamns.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author edwise
 * 
 */
public class ProjectSubprojectBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -8698388233039467693L;
	public static final String PROJECT_DEFAULT = "Selecciona proyecto...";
	public static final String SUBPROJECT_DEFAULT = "0 - Sin subcuenta";

	private List<String> projects = null;
	private Map<String, List<String>> projectsAndSubprojects = null;

	public ProjectSubprojectBean(List<String> projects, Map<String, List<String>> projectsAndSub) {
		this.projects = projects;
		this.projectsAndSubprojects = projectsAndSub;
	}

	public List<String> getProjects() {
		return projects;
	}

	public List<String> getSubProjects(String project) {
		List<String> subProjects = null;
		if (project != null) {
			subProjects = projectsAndSubprojects.get(project);
		} else {
			subProjects = projectsAndSubprojects.get(PROJECT_DEFAULT);
		}

		return subProjects;
	}

	public static List<String> createSubProjectsDefault() {
		List<String> subProjects = new ArrayList<String>();
		subProjects.add(SUBPROJECT_DEFAULT);

		return subProjects;
	}
}
