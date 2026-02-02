#!/usr/bin/env bash
set -e

# ============================
# CONFIGURATION
# ============================

OUTPUT_FILE="project-export.txt"
ROOT_DIR="${1:-.}"

# Extensions à inclure
INCLUDE_EXTENSIONS="java,kt,xml,yml,yaml,json,js,jsx,ts,tsx,css,scss,html,md,gradle,vue"

# Dossiers à exclure
EXCLUDES="node_modules|.git|target|build|dist|out|venv|__pycache__|.idea|.vscode"

# ============================
# FONCTIONS
# ============================

scan_tree() {
  local dir="$1"
  local prefix="$2"

  for entry in "$dir"/*; do
    [[ ! -e "$entry" ]] && continue

    # Exclusions
    if [[ "$entry" =~ $EXCLUDES ]] || [[ "$(basename "$entry")" == .* ]]; then
      continue
    fi

    if [ -d "$entry" ]; then
      echo "${prefix}📁 $(basename "$entry")" >> "$OUTPUT_FILE"
      scan_tree "$entry" "$prefix  "
    elif [ -f "$entry" ]; then
      echo "${prefix}📄 $(basename "$entry")" >> "$OUTPUT_FILE"
    fi
  done
}

export_files() {
  find "$ROOT_DIR" -type f \
    | grep -E "\.($(echo "$INCLUDE_EXTENSIONS" | sed 's/,/|/g'))$" \
    | grep -Ev "$EXCLUDES" \
    | while read -r file; do
        echo "========================================" >> "$OUTPUT_FILE"
        echo "📄 FILE: $file" >> "$OUTPUT_FILE"
        echo "========================================" >> "$OUTPUT_FILE"
        cat "$file" >> "$OUTPUT_FILE"
        echo -e "\n\n" >> "$OUTPUT_FILE"
      done
}

# ============================
# EXECUTION
# ============================

echo "📦 EXPORT DU PROJET" > "$OUTPUT_FILE"
echo "📁 Racine: $ROOT_DIR" >> "$OUTPUT_FILE"
echo "📅 Généré le: $(date)" >> "$OUTPUT_FILE"
echo "========================================" >> "$OUTPUT_FILE"
echo "" >> "$OUTPUT_FILE"

echo "📂 STRUCTURE DU PROJET" >> "$OUTPUT_FILE"
echo "----------------------------------------" >> "$OUTPUT_FILE"
scan_tree "$ROOT_DIR" ""
echo "" >> "$OUTPUT_FILE"

echo "📜 CONTENU DES FICHIERS" >> "$OUTPUT_FILE"
echo "----------------------------------------" >> "$OUTPUT_FILE"
export_files

echo "✅ Export terminé → $OUTPUT_FILE"
