package com.neostencil.utils;

import java.util.Collection;
import java.util.Iterator;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;
import com.neostencil.model.entities.TeacherDetails;

/**
 * For returning the multiple instructor case in hibernate search
 * 
 * @author shilpa
 *
 */

public class InstructorNameBridge implements TwoWayFieldBridge {

	@Override
	public String objectToString(Object object) {
		if (object == null) {
			return null;
		}
		if (!(object instanceof String)) {
			throw new IllegalArgumentException("This FieldBridge only supports String properties.");
		}
		return (String) object;
	}

	@Override
	public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {

		Collection<TeacherDetails> tdList = (Collection<TeacherDetails>) value;
		StringBuilder fieldValueBuilder = new StringBuilder();
		String fieldValue = "";
		if (tdList != null && !tdList.isEmpty() && tdList.size() > 1) {

			for (TeacherDetails td : tdList) {
				fieldValueBuilder.append(td.getTeacherName());
				fieldValueBuilder.append(", ");
			}
			fieldValue = fieldValueBuilder.toString();
		} else {
			Iterator it = tdList.iterator();
			if (it.hasNext()) {
				fieldValue = ((TeacherDetails) it.next()).getTeacherName();

			}
		}

		if (name != null && fieldValue != null && luceneOptions != null && luceneOptions.getStore() != null
				&& luceneOptions.getIndex() != null && luceneOptions.getTermVector() != null) {
			luceneOptions.addFieldToDocument(name, fieldValue, document);
		}
	}

	@Override
	public Object get(String name, Document document) {
		return document.get(name);
	}

}
