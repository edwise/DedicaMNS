/**
 * 
 */
package com.edwise.dedicamns.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.edwise.dedicamns.beans.ProjectSubprojectBean;

/**
 * @author edwise
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String LOGTAG = DatabaseHelper.class.toString();

	private static final String DATABASE_NAME = "accounts";
	private static final int DATABASE_VERSION = 1;

	private static final String PROJECTS_TABLE = "projects";
	private static final String PROJECT_ID_COLUMN = "_id";
	private static final String PROJECT_NAME_COLUMN = "name";

	private static final String SUB_PROJECTS_TABLE = "subprojects";
	private static final String SUB_PROJECT_ID_COLUMN = "_id";
	private static final String SUB_PROJECT_NAME_COLUMN = "name";
	private static final String SUB_PROJECT_PROJECT_ID_COLUMN = "projectId";

	private static final String CREATE_PROJECTS_TABLE = "CREATE TABLE " + PROJECTS_TABLE + " (" + PROJECT_ID_COLUMN
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ PROJECT_NAME_COLUMN + " TEXT);";
	private static final String CREATE_SUB_PROJECTS_TABLE = "CREATE TABLE " + SUB_PROJECTS_TABLE + " ("
			+ SUB_PROJECT_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SUB_PROJECT_NAME_COLUMN + " TEXT, "
			+ SUB_PROJECT_PROJECT_ID_COLUMN + " INTEGER NOT NULL , "
			+ "FOREIGN KEY (" + SUB_PROJECT_PROJECT_ID_COLUMN + ") REFERENCES " + PROJECTS_TABLE + " ("
			+ PROJECT_ID_COLUMN + "));";

	private static final String QUERY_GET_SUB_PROJECTS_FOR_PROJECT;
	private static final String QUERY_GET_ALL_PROJECTS_AND_SUB_PROJECTS;
	private static final String QUERY_IS_EMPTY;

	static {
		StringBuilder querySubProjectsForProject = new StringBuilder();
		querySubProjectsForProject.append("SELECT S.");
		querySubProjectsForProject.append(SUB_PROJECT_NAME_COLUMN);
		querySubProjectsForProject.append(" FROM ");
		querySubProjectsForProject.append(SUB_PROJECTS_TABLE).append(" S, ");
		querySubProjectsForProject.append(PROJECTS_TABLE).append(" P");
		querySubProjectsForProject.append(" WHERE ");
		querySubProjectsForProject.append("P.").append(PROJECT_ID_COLUMN).append(" = S.")
				.append(SUB_PROJECT_PROJECT_ID_COLUMN);
		querySubProjectsForProject.append(" AND ");
		querySubProjectsForProject.append("P.").append(PROJECT_NAME_COLUMN).append(" = ?");
		querySubProjectsForProject.append(" ORDER BY 1");
		QUERY_GET_SUB_PROJECTS_FOR_PROJECT = querySubProjectsForProject.toString();

		StringBuilder queryProjectsAndSubProjects = new StringBuilder();
		queryProjectsAndSubProjects.append("SELECT P.");
		queryProjectsAndSubProjects.append(PROJECT_NAME_COLUMN).append(", S.");
		queryProjectsAndSubProjects.append(SUB_PROJECT_NAME_COLUMN);
		queryProjectsAndSubProjects.append(" FROM ");
		queryProjectsAndSubProjects.append(PROJECTS_TABLE).append(" P, ");
		queryProjectsAndSubProjects.append(SUB_PROJECTS_TABLE).append(" S where P.");
		queryProjectsAndSubProjects.append(PROJECT_ID_COLUMN).append(" = S.").append(SUB_PROJECT_PROJECT_ID_COLUMN);
		queryProjectsAndSubProjects.append(" ORDER BY 1, 2");
		QUERY_GET_ALL_PROJECTS_AND_SUB_PROJECTS = queryProjectsAndSubProjects.toString();

		StringBuilder queryIsEmpty = new StringBuilder();
		queryIsEmpty.append("SELECT COUNT(");
		queryIsEmpty.append(PROJECT_ID_COLUMN).append(")");
		queryIsEmpty.append(" FROM ");
		queryIsEmpty.append(PROJECTS_TABLE);
		QUERY_IS_EMPTY = queryIsEmpty.toString();
	}

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(LOGTAG, "Creando DB...");
		db.execSQL(CREATE_PROJECTS_TABLE);
		db.execSQL(CREATE_SUB_PROJECTS_TABLE);

		Log.d(LOGTAG, "DB Creada!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(LOGTAG, "Actualizando DB...");

		recreateDB(db);

		Log.d(LOGTAG, "DB Actualizada!");
	}

	private void recreateDB(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + SUB_PROJECTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + PROJECTS_TABLE);

		onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			// Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	public void insertProject(String projectName, String[] subProjectsNames) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cvProject = new ContentValues();
		cvProject.put(PROJECT_NAME_COLUMN, projectName);

		// Este id es el row id, que es la primary! (al ser integer primary key, es un alias del rowid)
		long projectId = db.insert(PROJECTS_TABLE, null, cvProject);

		Log.d(LOGTAG, "Insertado proyecto: " + projectId + " - " + projectName);

		for (String subProject : subProjectsNames) {
			ContentValues cvSubProject = new ContentValues();
			cvSubProject.put(SUB_PROJECT_NAME_COLUMN, subProject);
			cvSubProject.put(SUB_PROJECT_PROJECT_ID_COLUMN, projectId);

			long subProjectid = db.insert(SUB_PROJECTS_TABLE, null, cvSubProject);

			Log.d(LOGTAG, "Insertado subproyecto: " + subProjectid + " - " + subProject + " - " + projectId);
		}

	}

	public List<String> getSubProjectsForProject(String projectName) {
		Log.d(LOGTAG, "getSubProjectsForProject...");
		List<String> result = new ArrayList<String>();
		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(QUERY_GET_SUB_PROJECTS_FOR_PROJECT, new String[] { projectName });
		while (cur.moveToNext()) {
			result.add(cur.getString(0));
		}
		cur.close();

		return result;
	}

	public ProjectSubprojectBean getAllProjectsAndSubProjects() {
		Log.d(LOGTAG, "getAllProjectsAndSubProjects...");
		long beginTime = System.currentTimeMillis();

		SQLiteDatabase db = getReadableDatabase();

		List<String> projects = new ArrayList<String>();
		Map<String, List<String>> projectsAndSubProjects = new HashMap<String, List<String>>();

		Cursor cur = db.rawQuery(QUERY_GET_ALL_PROJECTS_AND_SUB_PROJECTS, new String[] {});

		String actualProject = "";
		List<String> subProjects = null;
		while (cur.moveToNext()) {
			if (!actualProject.equals(cur.getString(0))) {
				if (subProjects != null) {
					projectsAndSubProjects.put(actualProject, subProjects);
				}
				actualProject = cur.getString(0);
				subProjects = new ArrayList<String>();
				projects.add(actualProject);
			}
			subProjects.add(cur.getString(1));
			if (cur.isLast()) {
				projectsAndSubProjects.put(actualProject, subProjects);
			}
		}
		cur.close();

		long endTime = System.currentTimeMillis();
		Log.d(LOGTAG, "Tiempo carga proyectos desde BD: " + (endTime - beginTime));

		return new ProjectSubprojectBean(projects, projectsAndSubProjects);
	}

	public boolean isEmptyDB() {
		Log.d(LOGTAG, "isEmptyDB...");
		boolean isEmpty = false;
		SQLiteDatabase db = getReadableDatabase();

		Cursor cur = db.rawQuery(QUERY_IS_EMPTY, new String[] {});
		if (cur.moveToNext()) {
			if (cur.getInt(0) == 0) {
				isEmpty = true;
			}

		}
		cur.close();

		return isEmpty;
	}

	public void deleteAllTablesData() {
		Log.d(LOGTAG, "deleteAllTablesData...");
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("DELETE FROM " + SUB_PROJECTS_TABLE);
		db.execSQL("DELETE FROM " + PROJECTS_TABLE);
	}
}
