/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.security.jackson2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Custom deserializer for {@link UnmodifiableListDeserializer}.
 *
 * @author Rob Winch
 * @see UnmodifiableListMixin
 * @since 5.0.2
 */
class UnmodifiableListDeserializer extends JsonDeserializer<List> {

	@Override
	public List deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode node = mapper.readTree(jp);
		List<Object> result = new ArrayList<Object>();
		if (node != null) {
			if (node instanceof ArrayNode) {
				ArrayNode arrayNode = (ArrayNode) node;
				Iterator<JsonNode> nodeIterator = arrayNode.iterator();
				while (nodeIterator.hasNext()) {
					JsonNode elementNode = nodeIterator.next();
					result.add(mapper.readValue(elementNode.traverse(mapper), Object.class));
				}
			} else {
				result.add(mapper.readValue(node.traverse(mapper), Object.class));
			}
		}
		return Collections.unmodifiableList(result);
	}
}
