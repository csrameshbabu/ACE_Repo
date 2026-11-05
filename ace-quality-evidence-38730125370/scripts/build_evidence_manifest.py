"""Create a compact, checksummed index for one ACE CI evidence bundle."""

from __future__ import annotations

import hashlib
import json
import os
import platform
import subprocess
import xml.etree.ElementTree as ET
from datetime import datetime, timezone
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]


def command_version(*command: str) -> str:
    try:
        result = subprocess.run(
            command, cwd=ROOT, check=False, capture_output=True, text=True, timeout=10
        )
        return (result.stdout + result.stderr).strip().splitlines()[0]
    except (OSError, subprocess.TimeoutExpired, IndexError):
        return "unavailable"


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as stream:
        for block in iter(lambda: stream.read(65536), b""):
            digest.update(block)
    return digest.hexdigest()


def main() -> None:
    report_paths = sorted((ROOT / "target/surefire-reports").glob("TEST-*.xml"))
    totals = {"tests": 0, "failures": 0, "errors": 0, "skipped": 0}
    suites = []
    for path in report_paths:
        root = ET.parse(path).getroot()
        suites.append(path.name)
        for key in totals:
            totals[key] += int(root.attrib.get(key, "0"))

    data_paths = [
        *(ROOT / "src/test/resources/data").glob("*"),
        *(ROOT / "src/test/resources/ai").glob("*"),
    ]
    manifest = {
        "schema_version": 1,
        "generated_utc": datetime.now(timezone.utc).isoformat(),
        "commit_sha": os.getenv("GITHUB_SHA", "local"),
        "workflow": os.getenv("GITHUB_WORKFLOW", "local"),
        "workflow_run_id": os.getenv("GITHUB_RUN_ID", "local"),
        "workflow_run_attempt": os.getenv("GITHUB_RUN_ATTEMPT", "local"),
        "event": os.getenv("GITHUB_EVENT_NAME", "local"),
        "platform": platform.platform(),
        "java_version": command_version("java", "-version"),
        "maven_version": command_version("mvn", "--version"),
        "test_reports_present": bool(report_paths),
        "test_totals": totals,
        "test_report_files": suites,
        "dataset_sha256": {
            path.relative_to(ROOT).as_posix(): sha256(path)
            for path in sorted(data_paths)
            if path.is_file()
        },
    }
    destination = ROOT / "target/evidence-manifest.json"
    destination.parent.mkdir(parents=True, exist_ok=True)
    destination.write_text(json.dumps(manifest, indent=2) + "\n", encoding="utf-8")
    print(f"Wrote {destination.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
