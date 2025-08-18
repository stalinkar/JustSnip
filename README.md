# ✂️ JustSnip

## 📸 What is JustSnip?

JustSnip is a simple yet powerful screen snipping tool built in Java!  
Easily capture screenshots and automatically save them into a `.docx` file.  
Record your work sessions and store them as `.mp4` videos for future reference.

---

## 🚀 Features

- 🖼️ **Screenshot Capture**: Instantly snip your screen and save directly to a Word (`.docx`) document.
- 📁 **Organized Storage**: All screenshots are saved under `User/Documents/JustSnip` for easy access.
- 🗃️ **Session Management**: All snapshots taken during an active session are saved in the same file.
- 🎥 **Screen Recording**: Record your entire workflow and save it as an `.mp4` file.

---

## 🎯 Intended Uses

- Document your development process.
- Create tutorials or guides with annotated screenshots.
- Keep a visual log of your troubleshooting steps.
- Record video walkthroughs of your work.

---

## 🏁 Getting Started

### 1. Prerequisites

- 🖥️ Java installed (JDK 8 or higher recommended)
- ⬇️ Download the latest release from [Releases](https://github.com/stalinkar/JustSnip/releases) *(if available)*

### 2. Installation

```sh
git clone https://github.com/stalinkar/JustSnip.git
cd JustSnip
```

### 3. Usage

#### Run the Application

```sh
# Compile the Java files
javac -d bin src/*.java

# Run the application
java -cp bin Main
```

#### Take Screenshots

- Hit the snip hotkey or use the in-app button to capture your screen.
- Screenshots are automatically saved in a `.docx` file under `User/Documents/JustSnip`.

#### Record Your Session

- Start recording from the app interface.
- When finished, your session will be saved as an `.mp4` file in the same folder.

---

## 📂 File Structure

```
JustSnip/
├── src/              # Source code
├── bin/              # Compiled binaries
├── README.md         # Project documentation
└── User/Documents/JustSnip/
    ├── screenshots.docx
    └── session.mp4
```

---

## ❓ FAQ

**Where are my screenshots saved?**  
Your screenshots are saved in `User/Documents/JustSnip/screenshots.docx`.

**Can I customize save locations?**  
Currently, all files are saved to the default folder for simplicity.

**Is video recording supported?**  
Yes! Record your work and save the output as `.mp4`.

---

## 🤝 Contributing

Pull requests, issues, and suggestions are welcome!  
Feel free to fork the repo and make improvements.

---

## 📜 License

This project is licensed under the MIT License.

---

## 💡 Author

Made with ❤️ by [stalinkar](https://github.com/stalinkar)
