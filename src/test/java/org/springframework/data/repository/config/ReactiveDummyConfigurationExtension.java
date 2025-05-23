/*
 * Copyright 2022-2025 the original author or authors.
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
package org.springframework.data.repository.config;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.ReactiveDummyRepositoryFactoryBean;

/**
 * @author Christoph Strobl
 * @since 2022/04
 */
class ReactiveDummyConfigurationExtension extends RepositoryConfigurationExtensionSupport {

	@Override
	public String getRepositoryFactoryBeanClassName() {
		return ReactiveDummyRepositoryFactoryBean.class.getName();
	}

	@Override
	public String getModulePrefix() {
		return "commons";
	}

	@Override
	protected boolean useRepositoryConfiguration(RepositoryMetadata metadata) {
		if(metadata.isReactiveRepository()) {
			return true;
		}
		return false;
	}
}
