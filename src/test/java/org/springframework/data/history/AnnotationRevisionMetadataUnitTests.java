/*
 * Copyright 2017-2025 the original author or authors.
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
package org.springframework.data.history;

import static org.assertj.core.api.Assertions.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;

/**
 * Unit tests for {@link AnnotationRevisionMetadata}.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 * @author Mark Paluch
 */
class AnnotationRevisionMetadataUnitTests {

	SoftAssertions softly = new SoftAssertions();

	@Test // DATACMNS-1173
	void exposesNoInformationOnEmptyProbe() {

		var sample = new Sample();
		var metadata = getMetadata(sample);

		assertThat(metadata.getRevisionNumber()).isEmpty();

		assertThatIllegalStateException() //
				.isThrownBy(metadata::getRequiredRevisionNumber);

		assertThatIllegalStateException() //
				.isThrownBy(metadata::getRequiredRevisionInstant);

	}

	@Test // DATACMNS-1173
	void exposesRevisionNumber() {

		var sample = new Sample();
		sample.revisionNumber = 1L;

		var metadata = getMetadata(sample);

		softly.assertThat(metadata.getRevisionNumber()).hasValue(1L);
		softly.assertThat(metadata.getRequiredRevisionNumber()).isEqualTo(1L);

		softly.assertAll();
	}

	@Test // DATACMNS-1173
	void exposesRevisionDateAndInstantForLocalDateTime() {

		var sample = new Sample();
		sample.revisionDate = LocalDateTime.now();
		var expectedInstant = sample.revisionDate.atZone(ZoneOffset.systemDefault()).toInstant();

		var metadata = getMetadata(sample);

		softly.assertThat(metadata.getRevisionInstant()).hasValue(expectedInstant);
		softly.assertThat(metadata.getRequiredRevisionInstant()).isEqualTo(expectedInstant);

		softly.assertAll();
	}

	@Test // GH-2569
	void exposesRevisionMetadataUsingMethodAccessors() {

		SampleWithMethodAnnotations sample = new SampleWithMethodAnnotations();
		sample.revisionNumber = 1L;
		sample.revisionDate = Instant.now();

		RevisionMetadata<Long> metadata = getMetadata(sample);

		softly.assertThat(metadata.getRevisionNumber()).hasValue(1L);
		softly.assertThat(metadata.getRevisionInstant()).hasValue(sample.revisionDate);

		softly.assertAll();
	}

	@Test // DATACMNS-1251
	void exposesRevisionDateAndInstantForInstant() {

		var sample = new SampleWithInstant();
		sample.revisionInstant = Instant.now();
		var expectedLocalDateTime = LocalDateTime.ofInstant(sample.revisionInstant, ZoneOffset.systemDefault());

		var metadata = getMetadata(sample);

		softly.assertThat(metadata.getRevisionInstant()).hasValue(sample.revisionInstant);
		softly.assertThat(metadata.getRequiredRevisionInstant()).isEqualTo(sample.revisionInstant);

		softly.assertAll();
	}

	@Test // DATACMNS-1290
	void exposesRevisionDateAndInstantForLong() {

		var sample = new SampleWithLong();
		sample.revisionLong = 4711L;

		var expectedInstant = Instant.ofEpochMilli(sample.revisionLong);
		var expectedLocalDateTime = LocalDateTime.ofInstant(expectedInstant, ZoneOffset.systemDefault());

		var metadata = getMetadata(sample);

		softly.assertThat(metadata.getRevisionInstant()).hasValue(expectedInstant);
		softly.assertThat(metadata.getRequiredRevisionInstant()).isEqualTo(expectedInstant);

		softly.assertAll();
	}

	@Test // DATACMNS-1384
	void supportsTimestampRevisionInstant() {

		var sample = new SampleWithTimestamp();
		var now = Instant.now();
		sample.revision = Timestamp.from(now);

		var metadata = getMetadata(sample);

		assertThat(metadata.getRequiredRevisionInstant()).isEqualTo(now);
	}

	@Test // DATACMNS-1384
	void supportsDateRevisionInstant() {

		var sample = new SampleWithDate();
		var date = new Date();
		sample.revision = date;

		var metadata = getMetadata(sample);

		assertThat(metadata.getRequiredRevisionInstant()).isEqualTo(date.toInstant());
	}

	private static RevisionMetadata<Long> getMetadata(Object sample) {
		return new AnnotationRevisionMetadata<>(sample, Autowired.class, Reference.class);
	}

	static class Sample {

		@Autowired Long revisionNumber;
		@Reference LocalDateTime revisionDate;
	}

	static class SampleWithMethodAnnotations {

		Long revisionNumber;
		Instant revisionDate;

		@Autowired
		public Long getRevisionNumber() {
			return revisionNumber;
		}

		@Reference
		public Instant getRevisionDate() {
			return revisionDate;
		}
	}

	static class SampleWithInstant {

		@Autowired Long revisionNumber;
		@Reference Instant revisionInstant;
	}

	static class SampleWithLong {

		@Autowired Long revisionNumber;
		@Reference long revisionLong;
	}

	// DATACMNS-1384

	static class SampleWithTimestamp {
		@Reference Timestamp revision;
	}

	static class SampleWithDate {
		@Reference Date revision;
	}
}
