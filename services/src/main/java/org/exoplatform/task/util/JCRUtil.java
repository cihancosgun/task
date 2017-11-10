package org.exoplatform.task.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;

/**
 * Created by Cihan Coşgun Author : Cihan Coşgun cihan042012@gmail.com 11/9/17
 */
public class JCRUtil {

	private static final String META_NODETYPE = "adn:meta";
	private static final String SIZE_PROPERTY = "adn:size";
	private static final String TIMESTAMP_PROPERTY = "adn:timestamp";
	private static final String LOG_PROPERTY = "adn:log";

	public static Node addTaskFileToNode(FileItem item, String userName) throws Exception {
		Node fileNode = null;
		if (item != null) {
			String path = "exotaskfiles/";
			String filename = FilenameUtils.getName(item.getName());
			RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance()
					.getComponentInstanceOfType(RepositoryService.class);
			NodeHierarchyCreator nodeHierarchyCreator = (NodeHierarchyCreator) PortalContainer.getInstance()
					.getComponentInstanceOfType(NodeHierarchyCreator.class);

			SessionProvider sessionProvider = SessionProvider.createSystemProvider();

			Session session = sessionProvider.getSession("collaboration", repositoryService.getCurrentRepository());

			Node rootNode = session.getRootNode();

			if (!rootNode.hasNode("exo-task-files")) {
				rootNode.addNode("exo-task-files");
			}

			Node docNode = rootNode.getNode("exo-task-files");

			if (docNode.hasNode(filename)) {
				docNode.getNode(filename).remove();
			}

			fileNode = docNode.addNode(filename, "nt:file");
			Node jcrContent = fileNode.addNode("jcr:content", "nt:resource");
			jcrContent.setProperty("jcr:data", item.getInputStream());
			jcrContent.setProperty("jcr:lastModified", Calendar.getInstance());
			jcrContent.setProperty("jcr:encoding", "UTF-8");
			if (filename.endsWith(".jpg"))
				jcrContent.setProperty("jcr:mimeType", "image/jpeg");
			else if (filename.endsWith(".png"))
				jcrContent.setProperty("jcr:mimeType", "image/png");
			else if (filename.endsWith(".pdf"))
				jcrContent.setProperty("jcr:mimeType", "application/pdf");
			else if (filename.endsWith(".doc"))
				jcrContent.setProperty("jcr:mimeType", "application/vnd.ms-word");
			else if (filename.endsWith(".xls"))
				jcrContent.setProperty("jcr:mimeType", "application/vnd.ms-excel");
			else if (filename.endsWith(".ppt"))
				jcrContent.setProperty("jcr:mimeType", "application/vnd.ms-powerpoint");
			else if (filename.endsWith(".docx"))
				jcrContent.setProperty("jcr:mimeType",
						"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			else if (filename.endsWith(".xlsx"))
				jcrContent.setProperty("jcr:mimeType",
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			else if (filename.endsWith(".pptx"))
				jcrContent.setProperty("jcr:mimeType",
						"application/vnd.openxmlformats-officedocument.presentationml.presentation");
			else if (filename.endsWith(".odp"))
				jcrContent.setProperty("jcr:mimeType", "application/vnd.oasis.opendocument.presentation");
			else if (filename.endsWith(".odt"))
				jcrContent.setProperty("jcr:mimeType", "application/vnd.oasis.opendocument.text");
			else if (filename.endsWith(".ods"))
				jcrContent.setProperty("jcr:mimeType", "application/vnd.oasis.opendocument.spreadsheet");
			fileNode.setProperty("exo:lastModifier", userName);
			// updateTimestamp(docNode);
			// updateSize(docNode);
			// updateTimestamp(docNode.getParent());
			session.save();
			String uuid = fileNode.getUUID();

			// if (uuid != null) {
			//// path = "ApplicationData/Tags/";
			//// // Node fileNode = session.getNodeByUUID(uuid);
			//// Node tagNode = homeNode.getNode(path);
			//// Node linkNode = tagNode.addNode(filename, "exo:symlink");
			//// linkNode.setProperty("exo:uuid", uuid);
			//// linkNode.setProperty("exo:workspace", "collaboration");
			//// linkNode.setProperty("exo:primaryType", "nt:file");
			//// tagNode.save();
			//// updateTimestamp(tagNode);
			//// updateTimestamp(tagNode.getParent());
			//// updateTimestamp(homeNode);
			//// session.save();
			//
			// }

		}
		return fileNode;
	}

	public static void removeFileFromJCR(String uuid) throws Exception {
		RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance()
				.getComponentInstanceOfType(RepositoryService.class);
		NodeHierarchyCreator nodeHierarchyCreator = (NodeHierarchyCreator) PortalContainer.getInstance()
				.getComponentInstanceOfType(NodeHierarchyCreator.class);

		SessionProvider sessionProvider = SessionProvider.createSystemProvider();

		Session session = sessionProvider.getSession("collaboration", repositoryService.getCurrentRepository());

		Node rootNode = session.getRootNode();

		if (!rootNode.hasNode("exo-task-files")) {
			rootNode.addNode("exo-task-files");
		}

		Node docNode = rootNode.getNode("exo-task-files");

		Node fileNode = session.getNodeByUUID(uuid);
		if (fileNode != null) {
			fileNode.remove();
		}
		session.save();
	}

	public static InputStream getFileStreamFromJCR(String uuid) throws Exception {
		InputStream in = null;
		RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance()
				.getComponentInstanceOfType(RepositoryService.class);
		NodeHierarchyCreator nodeHierarchyCreator = (NodeHierarchyCreator) PortalContainer.getInstance()
				.getComponentInstanceOfType(NodeHierarchyCreator.class);

		SessionProvider sessionProvider = SessionProvider.createSystemProvider();

		Session session = sessionProvider.getSession("collaboration", repositoryService.getCurrentRepository());

		Node rootNode = session.getRootNode();

		if (!rootNode.hasNode("exo-task-files")) {
			rootNode.addNode("exo-task-files");
		}

		Node docNode = rootNode.getNode("exo-task-files");

		Node fileNode = session.getNodeByUUID(uuid);
		if (fileNode != null) {
			in = getStream(fileNode);
		}
		session.save();

		return in;
	}

	private static InputStream getStream(Node node) throws Exception {
		if (node.hasNode("jcr:content")) {
			Node contentNode = node.getNode("jcr:content");
			if (contentNode.hasProperty("jcr:data")) {
				InputStream inputStream = contentNode.getProperty("jcr:data").getStream();
				return inputStream;
			}
		}
		return null;
	}

	private static String getSpacePath(String space) {
		return "Groups/spaces/" + space;
	}

	private static void checkIfHasMetaType(Node node) throws Exception {
		if (!node.isNodeType(META_NODETYPE)) {
			node.addMixin(META_NODETYPE);
			node.save();
		}
	}

	public static Long updateTimestamp(Node node) throws Exception {
		checkIfHasMetaType(node);
		Long ts = System.currentTimeMillis();
		node.setProperty(TIMESTAMP_PROPERTY, ts);
		node.save();
		return ts;
	}

	public static void updateSize(Node node) throws Exception {
		checkIfHasMetaType(node);
		long size = node.getNodes().getSize();
		if (node.hasNode("exo:thumbnails"))
			size -= 1;
		System.out.println("Path=" + node.getPath() + " ; Size=" + size);
		node.setProperty(SIZE_PROPERTY, size);
		node.save();
	}
}
