package repo.exceptions

import models.PartId
import models.PartLock

class RepoConcurrencyException(id: PartId, expectedLock: PartLock, actualLock: PartLock?): RepoPartException(
    id,
    "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
