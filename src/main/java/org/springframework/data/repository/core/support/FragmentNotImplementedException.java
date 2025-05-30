/*
 * Copyright 2021-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.repository.core.support;

import org.springframework.data.repository.core.RepositoryCreationException;

/**
 * Exception thrown during repository creation or repository method invocation when invoking a repository method on a
 * fragment without an implementation.
 *
 * @author Mark Paluch
 * @since 2.5
 */
@SuppressWarnings("serial")
public class FragmentNotImplementedException extends RepositoryCreationException {

	private final RepositoryFragment<?> fragment;

	/**
	 * Constructor for FragmentNotImplementedException.
	 *
	 * @param msg the detail message.
	 * @param repositoryInterface the repository interface.
	 * @param fragment the offending repository fragment.
	 */
	public FragmentNotImplementedException(String msg, Class<?> repositoryInterface, RepositoryFragment<?> fragment) {
		super(msg, repositoryInterface);
		this.fragment = fragment;
	}

	public RepositoryFragment<?> getFragment() {
		return fragment;
	}
}
