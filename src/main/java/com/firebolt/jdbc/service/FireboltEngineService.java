package com.firebolt.jdbc.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.firebolt.jdbc.client.account.FireboltAccountClient;
import com.firebolt.jdbc.connection.settings.FireboltProperties;
import com.firebolt.jdbc.exception.FireboltException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class FireboltEngineService {
	private final FireboltAccountClient fireboltAccountClient;

	public String getEngineHost(String connectionUrl, FireboltProperties loginProperties, String accessToken)
			throws FireboltException {
		String accountId = null;
		try {
			if (StringUtils.isNotEmpty(loginProperties.getAccount())) {
				accountId = fireboltAccountClient.getAccountId(connectionUrl, loginProperties.getAccount(), accessToken)
						.orElse(null);
			}
			if (StringUtils.isEmpty(loginProperties.getEngine()))
				return fireboltAccountClient.getDbDefaultEngineAddress(connectionUrl, accountId,
						loginProperties.getDatabase(), accessToken);
			String engineID = fireboltAccountClient.getEngineId(connectionUrl, accountId, loginProperties.getEngine(),
					accessToken);
			return fireboltAccountClient.getEngineAddress(connectionUrl, accountId, loginProperties.getEngine(),
					engineID, accessToken);
		} catch (FireboltException e) {
			throw e;
		} catch (Exception e) {
			throw new FireboltException(String.format("Could not get engine host at %s", connectionUrl), e);
		}
	}

	public String getEngineNameFromHost(String engineHost) throws FireboltException {
		return Optional.ofNullable(engineHost).filter(host -> host.contains(".")).map(host -> host.split("\\.")[0])
				.map(host -> host.replace("-", "_")).orElseThrow(() -> new FireboltException(
						String.format("Could not establish the engine from the host: %s", engineHost)));
	}
}
