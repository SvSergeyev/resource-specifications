package config

import CorSettings
import IAppSettings
import PartProcessor

data class AppSettings(
    override val corSettings: CorSettings,
    override val processor: PartProcessor,
): IAppSettings
