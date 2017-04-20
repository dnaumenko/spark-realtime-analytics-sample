CREATE KEYSPACE spark_test WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE TABLE spark_test.devices (
  device_id uuid PRIMARY KEY,
  some_nullable_field text,
  device_model text,
  device_memory_in_mb int,
  language text,
  ip_country text,
  received_at timestamp
);

INSERT INTO spark_test.devices(device_id, platform_name, device_model, device_memory_in_mb, language, ip_country, received_at)
VALUES (df378a8e-4fec-46da-bc0d-033b413521da, 'Google Play', 'LENOVO Lenovo A319', 1024, 'English', 'US', '2017-04-03T07:50:05.291Z');
INSERT INTO spark_test.devices(device_id, platform_name, device_model, device_memory_in_mb, language, ip_country, received_at)
VALUES (6d8bc425-fafa-4e76-a1bc-80b1fdd04d32, 'Google Play', 'Samsung GT-N7000', 512, 'English', 'US', '2017-04-03T08:51:05.291Z');
INSERT INTO spark_test.devices(device_id, platform_name, device_model, device_memory_in_mb, language, ip_country, received_at)
VALUES (82099f5b-49d7-431f-804f-ad3e41254eb3, '', 'Motorola XT1033', 512, 'English', 'GB', '2017-04-03T08:51:25.291Z');
INSERT INTO spark_test.devices(device_id, platform_name, device_model, device_memory_in_mb, language, ip_country, received_at)
VALUES (1bb6ba24-02b6-4c0e-9a1a-d14f08081906, 'Google Play', 'Motorola XT1033', 700, 'Portuguese', 'BR', '2017-04-03T09:52:25.291Z');
INSERT INTO spark_test.devices(device_id, platform_name, device_model, device_memory_in_mb, language, ip_country, received_at)
VALUES (4319a022-0f0e-4ae7-b1d4-ff528a4a79ab, 'Google Play', 'CHERRY Touch XL 2', 700, 'Portuguese', 'BR', '2017-04-04T09:53:25.291Z');

CREATE TABLE spark_test.events (
  id uuid PRIMARY KEY,
  app_id uuid,
  device_id uuid,
  submitted_at timestamp,
  event_at timestamp,
  event_id int,
  app_version text,
  os_version text,
  attr_1 float,
  attr_2 float,
  attr_3 float
);

INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, df378a8e-4fec-46da-bc0d-033b413521da,
'2017-04-03T07:50:05.291Z', '2017-04-03T07:50:05.291Z', 4,
'0.1.0', 'Android OS 4.4.2', 1, 1, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, df378a8e-4fec-46da-bc0d-033b413521da,
'2017-04-03T07:51:05.291Z', '2017-04-03T07:51:05.291Z', 5,
'0.1.0', 'Android OS 4.4.2', 0, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, df378a8e-4fec-46da-bc0d-033b413521da,
'2017-04-03T07:51:05.291Z', '2017-04-03T07:53:05.291Z', 5,
'0.1.0', 'Android OS 4.4.2', 0, 0, 0);

INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 6d8bc425-fafa-4e76-a1bc-80b1fdd04d32,
'2017-04-03T07:50:15.291Z', '2017-04-03T07:50:15.291Z', 4,
'0.17.0', 'Android OS 5.1.1', 1, 1, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 6d8bc425-fafa-4e76-a1bc-80b1fdd04d32,
'2017-04-03T07:51:25.291Z', '2017-04-03T07:51:25.291Z', 5,
'0.17.0', 'Android OS 5.1.1', 0, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 6d8bc425-fafa-4e76-a1bc-80b1fdd04d32,
'2017-04-03T07:56:05.291Z', '2017-04-03T07:56:05.291Z', 5,
'0.17.0', 'Android OS 5.1.1', 0, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 6d8bc425-fafa-4e76-a1bc-80b1fdd04d32,
'2017-04-03T07:59:05.291Z', '2017-04-03T07:59:05.291Z', 5,
'0.17.0', 'Android OS 5.1.1', 0, 0, 0);

INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 82099f5b-49d7-431f-804f-ad3e41254eb3,
'2017-04-03T07:52:05.291Z', '2017-04-03T07:52:05.291Z', 4,
'0.17.0', 'Android OS 4.4.2', 1, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 82099f5b-49d7-431f-804f-ad3e41254eb3,
'2017-04-03T07:53:05.291Z', '2017-04-03T07:53:05.291Z', 5,
'0.17.0', 'Android OS 4.4.2', 0, 0, 0);

INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 1bb6ba24-02b6-4c0e-9a1a-d14f08081906,
'2017-04-04T08:52:05.291Z', '2017-04-04T08:52:05.291Z', 4,
'0.17.0', 'Android OS 5.1', 1, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 1bb6ba24-02b6-4c0e-9a1a-d14f08081906,
'2017-04-04T08:53:05.291Z', '2017-04-04T08:53:05.291Z', 5,
'0.17.0', 'Android OS 5.1', 0, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 1bb6ba24-02b6-4c0e-9a1a-d14f08081906,
'2017-04-04T08:53:05.291Z', '2017-04-04T08:53:05.291Z', 9,
'0.17.0', 'Android OS 5.1', 0, 2.23, 0);

INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 4319a022-0f0e-4ae7-b1d4-ff528a4a79ab,
'2017-04-04T11:52:05.291Z', '2017-04-04T11:52:05.291Z', 4,
'0.17.0', 'Android OS 5.1.1', 1, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 4319a022-0f0e-4ae7-b1d4-ff528a4a79ab,
'2017-04-04T11:53:05.291Z', '2017-04-04T11:53:05.291Z', 5,
'0.17.0', 'Android OS 5.1.1', 0, 0, 0);
INSERT INTO spark_test.events (id, app_id, device_id, submitted_at, event_at, event_id, app_version, os_version, attr_1, attr_2, attr_3)
VALUES (uuid(), d4943de6-4eed-4fc5-80e3-36d547a7224d, 4319a022-0f0e-4ae7-b1d4-ff528a4a79ab,
'2017-04-04T11:53:05.291Z', '2017-04-04T11:53:05.291Z', 9,
'0.17.0', 'Android OS 5.1.1', 0, 0.17, 0);
