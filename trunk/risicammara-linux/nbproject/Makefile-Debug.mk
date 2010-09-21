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
CC=gcc
CCC=g++
CXX=g++
FC=
AS=as

# Macros
CND_PLATFORM=GNU-Linux-x86
CND_CONF=Debug
CND_DISTDIR=dist

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/main.o \
	${OBJECTDIR}/debug_console_messages.o \
	${OBJECTDIR}/plancia.o \
	${OBJECTDIR}/mazzo.o \
	${OBJECTDIR}/armate.o \
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
	"${MAKE}"  -f nbproject/Makefile-Debug.mk dist/Debug/GNU-Linux-x86/risicammara-linux

dist/Debug/GNU-Linux-x86/risicammara-linux: ${OBJECTFILES}
	${MKDIR} -p dist/Debug/GNU-Linux-x86
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/risicammara-linux ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/main.o: main.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/main.o main.cpp

${OBJECTDIR}/debug_console_messages.o: debug_console_messages.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/debug_console_messages.o debug_console_messages.cpp

${OBJECTDIR}/plancia.o: plancia.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/plancia.o plancia.cpp

${OBJECTDIR}/mazzo.o: mazzo.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/mazzo.o mazzo.cpp

${OBJECTDIR}/armate.o: armate.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/armate.o armate.cpp

${OBJECTDIR}/giocatori.o: giocatori.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/giocatori.o giocatori.cpp

${OBJECTDIR}/fasi_gioco.o: fasi_gioco.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} $@.d
	$(COMPILE.cc) -g -Werror -DDEBUG -MMD -MP -MF $@.d -o ${OBJECTDIR}/fasi_gioco.o fasi_gioco.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r build/Debug
	${RM} dist/Debug/GNU-Linux-x86/risicammara-linux

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
