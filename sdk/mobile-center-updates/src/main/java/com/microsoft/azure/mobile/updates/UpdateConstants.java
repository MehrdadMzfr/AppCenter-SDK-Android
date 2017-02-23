package com.microsoft.azure.mobile.updates;

import android.app.DownloadManager;
import android.support.annotation.VisibleForTesting;

import com.microsoft.azure.mobile.MobileCenter;

/**
 * Updates constants.
 */
final class UpdateConstants {

    /**
     * Update service name.
     */
    static final String SERVICE_NAME = "Updates";

    /**
     * Log tag for this service.
     */
    static final String LOG_TAG = MobileCenter.LOG_TAG + SERVICE_NAME;

    /**
     * Used for deep link intent from browser, string field for update token.
     */
    static final String EXTRA_UPDATE_TOKEN = "update_token";

    /**
     * Used for deep link intent from browser, string field for request identifier.
     */
    static final String EXTRA_REQUEST_ID = "request_id";

    /**
     * Base URL used to open browser to check install and get API token to check latest release.
     */
    static final String DEFAULT_INSTALL_URL = "https://install.mobile.azure.com";

    /**
     * Base URL to call server to check latest release.
     */
    static final String DEFAULT_API_URL = "https://api.mobile.azure.com";

    /**
     * Update setup URL path. Contains the app secret variable to replace.
     * Trailing slash needed to avoid redirection that can lose the query string on some servers.
     */
    static final String UPDATE_SETUP_PATH_FORMAT = "/apps/%s/update-setup/";

    /**
     * Check latest release API URL path. Contains the app secret variable to replace.
     */
    static final String GET_LATEST_RELEASE_PATH_FORMAT = "/sdk/apps/%s/releases/latest";

    /**
     * API parameter for release hash.
     */
    static final String PARAMETER_RELEASE_HASH = "release_hash";

    /**
     * API parameter for redirect URL.
     */
    static final String PARAMETER_REDIRECT_ID = "redirect_id";

    /**
     * API parameter for request identifier.
     */
    static final String PARAMETER_REQUEST_ID = "request_id";

    /**
     * API parameter for platform.
     */
    static final String PARAMETER_PLATFORM = "platform";

    /**
     * API parameter value for this platform.
     */
    static final String PARAMETER_PLATFORM_VALUE = "Android";

    /**
     * Header used to pass token when checking latest release.
     */
    static final String HEADER_API_TOKEN = "x-api-token";

    /**
     * Invalid release identifier.
     */
    static final int INVALID_RELEASE_IDENTIFIER = -1;

    /**
     * Invalid download identifier.
     */
    static final long INVALID_DOWNLOAD_IDENTIFIER = -1;

    /**
     * After we show install U.I, the download is mark completed but we keep the file.
     * No download is also using this value.
     */
    static final int DOWNLOAD_STATE_COMPLETED = 0;

    /**
     * We are waiting to hear back from download manager, we may poll status on process restart.
     */
    static final int DOWNLOAD_STATE_ENQUEUED = 1;

    /**
     * Download is finished, notification was posted but user could ignore it,
     * we use that state to show install U.I 1 time when application is resumed.
     */
    static final int DOWNLOAD_STATE_NOTIFIED = 2;

    /**
     * Base key for stored preferences.
     */
    private static final String PREFERENCE_PREFIX = SERVICE_NAME + ".";

    /**
     * Preference key to store the current/last download identifier (we keep download until a next
     * one is scheduled as the file can be opened from device downloads U.I.).
     */
    static final String PREFERENCE_KEY_DOWNLOAD_ID = PREFERENCE_PREFIX + "download_id";

    /**
     * Preference key to store the SDK state related to {@link #PREFERENCE_KEY_DOWNLOAD_ID} when not null.
     */
    static final String PREFERENCE_KEY_DOWNLOAD_STATE = PREFERENCE_PREFIX + "download_state";

    /**
     * Preference key for request identifier to validate deep link intent.
     */
    static final String PREFERENCE_KEY_REQUEST_ID = PREFERENCE_PREFIX + EXTRA_REQUEST_ID;

    /**
     * Preference key to store token.
     */
    static final String PREFERENCE_KEY_UPDATE_TOKEN = PREFERENCE_PREFIX + EXTRA_UPDATE_TOKEN;

    /**
     * Preference key to store ignored release id.
     */
    static final String PREFERENCE_KEY_IGNORED_RELEASE_ID = PREFERENCE_PREFIX + "ignored_release_id";

    /**
     * Preference key to store download start time. Used to avoid showing install U.I. of a completed
     * download if we already updated (the download workflow can work across process restarts).
     * <p>
     * We can't use {@link DownloadManager#COLUMN_LAST_MODIFIED_TIMESTAMP} as we could have a corner case
     * where we install upgrade from email or another mean while waiting download triggered by SDK.
     * So the time we store as a reference needs to be before download time.
     */
    static final String PREFERENCE_KEY_DOWNLOAD_TIME = PREFERENCE_PREFIX + "download_time";

    @VisibleForTesting
    UpdateConstants() {

        /* Hide constructor as it's just a constant class. */
    }
}