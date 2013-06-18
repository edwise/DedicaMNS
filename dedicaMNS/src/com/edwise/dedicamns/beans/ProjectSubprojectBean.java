/**
 * 
 */
package com.edwise.dedicamns.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.edwise.dedicamns.R;
import com.edwise.dedicamns.asynctasks.AppData;

/**
 * @author edwise
 * 
 */
public class ProjectSubprojectBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8698388233039467693L;

	private List<String> projects = null;
	private Map<String, List<String>> projectsAndSubprojects = null;

	public ProjectSubprojectBean(List<String> projects, Map<String, List<String>> projectsAndSub) {
		this.projects = projects;
		this.projectsAndSubprojects = projectsAndSub;

		String defaultProject = AppData.getCurrentActivity().getString(R.string.defaultProject);
		String defaultSubProject = AppData.getCurrentActivity().getString(R.string.defaultSubProject);

		// Añadimos la opción por defecto
		this.projects.add(0, defaultProject);
		List<String> subProjects = new ArrayList<String>();
		subProjects.add(defaultSubProject);
		projectsAndSubprojects.put(defaultProject, subProjects);
	}

	public List<String> getProjects() {
		return projects;
	}

	public List<String> getSubProjects(String project) {
		List<String> subProjects = null;
		if (project != null) {
			subProjects = projectsAndSubprojects.get(project);
		} else {
			subProjects = projectsAndSubprojects.get(AppData.getCurrentActivity().getString(R.string.defaultProject));
		}

		return subProjects;
	}

}
