/*******************************************************************************
 * Copyright (c) 2015 - 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package jsettlers.logic.map.grid.partition.data;

import static java8.util.stream.StreamSupport.stream;

import jsettlers.common.buildings.EBuildingType;
import jsettlers.common.map.partition.IBuildingCounts;
import jsettlers.logic.buildings.Building;

class BuildingCounts implements IBuildingCounts {

	private final int[] buildingsInPartitionUnderConstruction = new int[EBuildingType.NUMBER_OF_BUILDINGS];
	private final int[] buildingsInPartition = new int[EBuildingType.NUMBER_OF_BUILDINGS];
	private final int[] buildingsUnderConstruction = new int[EBuildingType.NUMBER_OF_BUILDINGS];
	private final int[] buildings = new int[EBuildingType.NUMBER_OF_BUILDINGS];
	private final byte playerId;

	public BuildingCounts(byte playerId, short partitionId) {
		this.playerId = playerId;
		stream(Building.getAllBuildings()).filter(building -> building.getPlayer().getPlayerId() == playerId).forEach(building -> {
			int buildingTypeIdx = building.getBuildingType().ordinal;
			boolean finishedConstruction = building.isConstructionFinished();

			if (finishedConstruction) {
				buildings[buildingTypeIdx]++;
			} else {
				buildingsUnderConstruction[buildingTypeIdx]++;
			}

			if (building.getPartitionId() == partitionId) {
				if (finishedConstruction) {
					buildingsInPartition[buildingTypeIdx]++;
				} else {
					buildingsInPartitionUnderConstruction[buildingTypeIdx]++;
				}
			}
		});
	}

	@Override
	public int buildingsInPartitionUnderConstruction(EBuildingType buildingType) {
		if (playerId == 1)
		return buildingsInPartitionUnderConstruction[buildingType.ordinal];
		return 0;
	}

	@Override
	public int buildingsInPartition(EBuildingType buildingType) {
		if (playerId == 1)
		return buildingsInPartition[buildingType.ordinal];
		return 0;
	}

	@Override
	public int buildingsUnderConstruction(EBuildingType buildingType) {
		if (playerId == 1)
		return buildingsUnderConstruction[buildingType.ordinal];
		return 0;
	}

	@Override
	public int buildings(EBuildingType buildingType) {
		if (playerId == 1)
		return buildings[buildingType.ordinal];
		return 0;
	}
}