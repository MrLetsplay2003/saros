package saros.negotiation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import saros.filesystem.IReferencePoint;

/**
 * Defines which projects and which of their resources) to share during a particular {@link
 * AbstractOutgoingResourceNegotiation}.
 */
public class ProjectSharingData implements Iterable<IReferencePoint> {

  private Map<String, IReferencePoint> projectsById = new HashMap<>();
  private Map<IReferencePoint, String> idsByProject = new HashMap<>();

  /**
   * Declares that the passed project should be shared with the specified ID.
   *
   * @param project project that should be shared
   * @param projectId session-wide ID assigned to the project
   */
  public void addProject(IReferencePoint project, String projectId) {
    projectsById.put(projectId, project);
    idsByProject.put(project, projectId);
  }

  /**
   * Returns the to-be-shared project with the passed ID.
   *
   * @param id project ID
   * @return project with the ID
   */
  public IReferencePoint getProject(String id) {
    return projectsById.get(id);
  }

  /**
   * Returns if the project is already contained in this collection
   *
   * @param id projectId
   * @return boolean indicating if the project is already contained in this collection
   */
  public boolean hasProjectById(String id) {
    return projectsById.containsKey(id);
  }

  /**
   * Returns if the project is already contained in this collection
   *
   * @param project
   * @return boolean indicating if the project is already contained in this collection
   */
  public boolean hasProject(IReferencePoint project) {
    return idsByProject.containsKey(project);
  }

  /**
   * Returns the ID of the given to-be-shared project.
   *
   * @param project one of the to-be-shared projects
   * @return matching project ID
   */
  public String getProjectID(IReferencePoint project) {
    return idsByProject.get(project);
  }

  /**
   * Returns the number of to-be-shared projects added to this {@link ProjectSharingData} instance.
   *
   * @return number of projects
   */
  public int size() {
    return projectsById.size();
  }

  /**
   * Returns whether this {@link ProjectSharingData} instance contains any to-be-shared projects.
   *
   * @return true if there are no projects, false if there are
   */
  public boolean isEmpty() {
    return projectsById.isEmpty();
  }

  @Override
  public Iterator<IReferencePoint> iterator() {
    return Collections.unmodifiableCollection(projectsById.values()).iterator();
  }
}
