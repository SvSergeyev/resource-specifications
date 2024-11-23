package repo.exceptions

import models.PartId

class RepoEmptyLockException(id: PartId): RepoPartException(
    id,
    "Lock is empty in DB"
)
