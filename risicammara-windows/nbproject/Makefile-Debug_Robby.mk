#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc.exe
CCC=g++.exe
CXX=g++.exe
FC=g77.exe
AS=as.exe

# Macros
CND_PLATFORM=MinGW-Windows
CND_CONF=Debug_Robby
CND_DISTDIR=dist

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/main.o \
	${OBJECTDIR}/plancia.o \
	${OBJECTDIR}/mazzo.o \
	${OBJECTDIR}/giocatori.o \
	${OBJECTDIR}/fasi_gioco.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-Debug_Robby.mk dist/Debug_Robby/MinGW-Windows/risicazzi-linux.exe

dist/Debug_Robby/MinGW-Windows/risicazzi-linux.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Debug_Robby/MinGW-Windows
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/risicazzi-linux ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/main.o: main.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -DDEBUG_ROB -MMD -MP -MF $@.d -o ${OBJECTDIR}/main.o main.cpp

${OBJECTDIR}/plancia.o: plancia.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -DDEBUG_ROB -MMD -MP -MF $@.d -o ${OBJECTDIR}/plancia.o plancia.cpp

${OBJECTDIR}/mazzo.o: mazzo.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -DDEBUG_ROB -MMD -MP -MF $@.d -o ${OBJECTDIR}/mazzo.o mazzo.cpp

${OBJECTDIR}/giocatori.o: giocatori.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -DDEBUG_ROB -MMD -MP -MF $@.d -o ${OBJECTDIR}/giocatori.o giocatori.cpp

${OBJECTDIR}/fasi_gioco.o: fasi_gioco.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -DDEBUG_ROB -MMD -MP -MF $@.d -o ${OBJECTDIR}/fasi_gioco.o fasi_gioco.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r build/Debug_Robby
	${RM} dist/Debug_Robby/MinGW-Windows/risicazzi-linux.exe

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
