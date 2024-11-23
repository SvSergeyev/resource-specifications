package exceptions

import models.WorkMode

class PartDbNotConfiguredException(val workMode: WorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
